package com.rishav.commerce.gateway;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A lightweight gateway-level, fixed-window rate limiter.
 *
 * This protects public API endpoints during local and single-instance deployments.
 * For multiple gateway instances, replace the in-memory map with a shared Redis-backed
 * limiter so every instance applies the same limit.
 */
@Component
class RequestThrottleFilter implements GlobalFilter, Ordered {

    private final ConcurrentHashMap<String, WindowCounter> clients = new ConcurrentHashMap<>();
    private final int requestsPerMinute;
    private final Counter rejectedRequests;

    RequestThrottleFilter(
            @Value("${app.throttle.requests-per-minute:60}") int requestsPerMinute,
            MeterRegistry meterRegistry) {
        if (requestsPerMinute < 1) {
            throw new IllegalArgumentException("app.throttle.requests-per-minute must be at least 1");
        }
        this.requestsPerMinute = requestsPerMinute;
        this.rejectedRequests = Counter.builder("gateway_throttle_rejections_total")
                .description("Requests rejected by the API gateway throttle")
                .register(meterRegistry);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        if (!request.getPath().value().startsWith("/api/") || request.getMethod() == null
                || request.getMethod().name().equals("OPTIONS")) {
            return chain.filter(exchange);
        }

        var result = clients.computeIfAbsent(clientKey(exchange), ignored -> new WindowCounter())
                .tryAcquire(requestsPerMinute);
        var response = exchange.getResponse();
        response.getHeaders().set("X-RateLimit-Limit", String.valueOf(requestsPerMinute));
        response.getHeaders().set("X-RateLimit-Remaining", String.valueOf(result.remaining()));

        if (result.allowed()) {
            return chain.filter(exchange);
        }

        rejectedRequests.increment();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().set(HttpHeaders.RETRY_AFTER, String.valueOf(result.retryAfterSeconds()));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        var body = "{\"error\":\"rate_limit_exceeded\",\"message\":\"Too many requests. Please retry shortly.\"}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))));
    }

    private String clientKey(ServerWebExchange exchange) {
        var forwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",", 2)[0].trim();
        }
        InetSocketAddress address = exchange.getRequest().getRemoteAddress();
        return address == null || address.getAddress() == null ? "unknown" : address.getAddress().getHostAddress();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private static final class WindowCounter {
        private long windowStartedAt = System.currentTimeMillis();
        private int count;

        synchronized ThrottleResult tryAcquire(int limit) {
            var now = System.currentTimeMillis();
            if (now - windowStartedAt >= 60_000) {
                windowStartedAt = now;
                count = 0;
            }

            var retryAfterSeconds = Math.max(1, (int) Math.ceil((60_000 - (now - windowStartedAt)) / 1_000.0));
            if (count >= limit) {
                return new ThrottleResult(false, 0, retryAfterSeconds);
            }
            count++;
            return new ThrottleResult(true, limit - count, retryAfterSeconds);
        }
    }

    private record ThrottleResult(boolean allowed, int remaining, int retryAfterSeconds) {
    }
}
