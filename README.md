# Event-Driven Commerce Platform

Resume-ready Java microservices project with a React operations console, Spring Boot, Kafka, OAuth2/JWT security, Docker, and automated GitHub Actions quality gates.

## Repository layout

- `frontend/` — React, TypeScript, and Vite application.
- `backend/` — Gradle multi-module Spring Boot services.
- `.github/workflows/` — pull-request CI and tag-based release automation.

## Versioning and releases

Use Semantic Versioning: `MAJOR.MINOR.PATCH` (for example, `v0.1.0`). Update `VERSION`, `CHANGELOG.md`, and the frontend/backend version fields before tagging a release. Pushing a tag such as `v0.1.1` creates a GitHub Release with generated notes.
