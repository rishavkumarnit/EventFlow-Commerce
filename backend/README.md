# Commerce Platform Backend

The initial event-driven backend contains two independently deployable Spring Boot services:

- `order-service` receives secured order requests and emits `orders.created.v1` Kafka events.
- `inventory-service` consumes those events; the next implementation slice adds durable, idempotent stock reservations.

`common-events` is intentionally limited to versioned event contracts. It must never contain domain logic.

## Run locally

Start Kafka with `docker compose up -d`, then run `gradle :order-service:bootRun` and `gradle :inventory-service:bootRun`.

For local development without Keycloak, use the `local` profile for Order Service. This profile permits requests only on your computer; it must never be used in a deployed environment.

The order endpoint requires an OAuth2 access token with `SCOPE_orders.write`; use Keycloak or another OpenID Connect provider configured through `JWT_ISSUER_URI`.

## Planned services

API gateway, identity/Keycloak, product catalog, payment, notification, PostgreSQL + Flyway, Redis, Grafana/Prometheus, distributed tracing, and Testcontainers integration tests.
