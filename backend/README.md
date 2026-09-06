# Commerce Platform Backend

The initial event-driven backend contains two independently deployable Spring Boot services:

- `order-service` receives secured order requests and emits `orders.created.v1` Kafka events.
- `inventory-service` consumes those events; the next implementation slice adds durable, idempotent stock reservations.

`common-events` is intentionally limited to versioned event contracts. It must never contain domain logic.

## Run locally

Start infrastructure with `docker compose up -d`, then run `gradle :order-service:bootRun` and `gradle :inventory-service:bootRun`.

The API Gateway runs on port `8080` and is the single protected entry point for `/api/v1/orders/**` and `/api/v1/inventory/**`. Start it with `gradle :api-gateway:bootRun`.

Kafka uses the named `kafka-data` Docker volume, so topics and messages survive normal container recreation after the volume is initialized.

For local development without Keycloak, use the `local` profile for Order Service. This profile permits requests only on your computer; it must never be used in a deployed environment.

The compose setup includes Keycloak at `http://localhost:8081`. Its identity data is persisted in the separate `keycloak_db` PostgreSQL database. It imports the `commerce` realm, a `commerce-frontend` public client, and a local demo account: `demo-user` / `commerce123`. Self-registration and password reset are enabled, so the React **Create account** button can create additional local users.

The React app uses OAuth 2.0 / OpenID Connect Authorization Code flow with PKCE. The order endpoint validates the resulting JWT and requires `SCOPE_orders.write`; use Keycloak or another OpenID Connect provider configured through `JWT_ISSUER_URI`.

## Optional: Google sign-in

Copy `.env.example` to `.env`, add the Google OAuth client values, then run `.\scripts\configure-google-oauth.ps1`. The Google redirect URI must be `http://127.0.0.1:8081/realms/commerce/broker/google/endpoint`.


## Planned services

API gateway, identity/Keycloak, product catalog, payment, notification, PostgreSQL + Flyway, Redis, Grafana/Prometheus, distributed tracing, and Testcontainers integration tests.
