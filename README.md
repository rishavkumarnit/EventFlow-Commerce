# Event-Driven Commerce Platform

Resume-ready Java microservices project with a React operations console, Spring Boot, Kafka, OAuth2/JWT security, Docker, and automated GitHub Actions quality gates.

## Repository layout

- `frontend/` — React, TypeScript, and Vite application.
- `backend/` — Gradle multi-module Spring Boot services.
- `.github/workflows/` — pull-request CI and tag-based release automation.

## Versioning and releases

Use Semantic Versioning: `MAJOR.MINOR.PATCH` (for example, `v0.1.0`). Update `VERSION`, `CHANGELOG.md`, and the frontend/backend version fields before tagging a release. Pushing a tag such as `v0.1.1` creates a GitHub Release with generated notes.

## Local monitoring

Prometheus collects health and JVM/application metrics from every Spring Boot service. Grafana displays those metrics.

Start the infrastructure and monitoring stack:

```powershell
cd backend
docker compose up -d
```

Then start the API Gateway and services locally. Open Grafana at `http://127.0.0.1:3000` and sign in with `admin` / `admin` (or set `GRAFANA_ADMIN_USER` and `GRAFANA_ADMIN_PASSWORD` before starting Docker). The Prometheus connection and the **EventFlow Commerce | Service Health** dashboard are provisioned automatically. Open it from **Dashboards → EventFlow Commerce** after refreshing the page.

## Interactive API documentation

After starting a service, Swagger UI provides interactive OpenAPI documentation:

- Order Service: `http://127.0.0.1:8082/swagger-ui/index.html`
- Inventory Service: `http://127.0.0.1:8083/swagger-ui/index.html`
- Product Catalog: `http://127.0.0.1:8084/swagger-ui/index.html`

Order creation requires a Keycloak access token with the `orders.write` scope. Products remain public so the React app can display the catalog before sign-in.
