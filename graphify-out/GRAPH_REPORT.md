# Graph Report - JAVAPROJECT1  (2026-09-06)

## Corpus Check
- Corpus is ~17,513 words - fits in a single context window. You may not need a graph.

## Summary
- 337 nodes · 550 edges · 32 communities (16 shown, 6 thin omitted)
- Extraction: 96% EXTRACTED · 4% INFERRED · 0% AMBIGUOUS · INFERRED: 21 edges (avg confidence: 0.84)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- Inventory API
- Frontend Dependencies
- Kafka Event Contracts
- Payment Checkout API
- Gateway Security and Docs
- Product Catalog API
- Inventory Domain Model
- Observability Data Sources
- Service Bootstraps
- Frontend Compiler Settings
- Graphify Tooling
- Node TypeScript Settings
- Commerce Service Architecture
- Docker Monitoring Stack
- Gradle Wrapper
- Payment Service Bootstrap
- React Operations Frontend
- Root TypeScript Settings
- Graphify Semantic Extraction
- Continuous Integration
- Release Workflow
- Keycloak Authentication

## God Nodes (most connected - your core abstractions)
1. `Order` - 18 edges
2. `PaymentController` - 13 edges
3. `Product` - 13 edges
4. `OrderCreatedEvent` - 12 edges
5. `Payment` - 12 edges
6. `PaymentProcessor` - 12 edges
7. `InventoryItem` - 11 edges
8. `RazorpayService` - 11 edges
9. `compilerOptions` - 11 edges
10. `InventoryReservationService` - 10 edges

## Surprising Connections (you probably didn't know these)
- `Project Graphify Guidance` --references--> `Graphify`  [EXTRACTED]
  AGENTS.md → .codex/skills/graphify/SKILL.md
- `Event-Driven Commerce Platform` --conceptually_related_to--> `Inventory Service`  [EXTRACTED]
  README.md → backend/README.md
- `Event-Driven Commerce Platform` --conceptually_related_to--> `Order Service`  [EXTRACTED]
  README.md → backend/README.md
- `Local Monitoring` --conceptually_related_to--> `Observability Stack`  [EXTRACTED]
  README.md → backend/docker-compose.yml
- `Order Service Runtime Configuration` --semantically_similar_to--> `Order Service Configuration`  [INFERRED] [semantically similar]
  backend/order-service/bin/main/application.yml → backend/order-service/src/main/resources/application.yml

## Import Cycles
- None detected.

## Communities (32 total, 6 thin omitted)

### Community 0 - "Inventory API"
Cohesion: 0.07
Nodes (14): InventoryController, InventoryItemRepository, ProcessedEventRepository, CreateOrderRequest, OrderController, OrderResponse, Order, OrderRepository (+6 more)

### Community 1 - "Frontend Dependencies"
Cohesion: 0.06
Nodes (37): dependencies, keycloak-js, react, react-dom, devDependencies, @types/react, @types/react-dom, typescript (+29 more)

### Community 2 - "Kafka Event Contracts"
Cohesion: 0.12
Nodes (18): InventoryReservationFailedEvent, InventoryReservedEvent, OrderCreatedEvent, PaymentCompletedEvent, InventoryReservationService, OrderCreatedListener, InventoryReservedListener, OrderOutboxService (+10 more)

### Community 3 - "Payment Checkout API"
Cohesion: 0.12
Nodes (15): Checkout, PaymentController, Verify, Entity, Table, Payment, PaymentRepository, PaymentProcessor (+7 more)

### Community 4 - "Gateway Security and Docs"
Cohesion: 0.15
Nodes (17): OpenAPI, OpenApiConfiguration, SecurityConfiguration, LocalSecurityConfiguration, SecurityConfiguration, OpenAPI, OpenApiConfiguration, io.swagger.v3.oas.models.OpenAPI (+9 more)

### Community 5 - "Product Catalog API"
Cohesion: 0.11
Nodes (9): GetMapping, RequestMapping, RestController, ProductController, ProductResponse, Entity, Table, Product (+1 more)

### Community 6 - "Inventory Domain Model"
Cohesion: 0.14
Nodes (6): InventoryResponse, InventoryItem, ProcessedEvent, OutboxEvent, jakarta.persistence.Entity, jakarta.persistence.Table

### Community 7 - "Observability Data Sources"
Cohesion: 0.11
Nodes (19): Grafana Loki Datasource, Grafana Prometheus Datasource, Loki Filesystem Storage, Loki Server, API Gateway Metrics Target, Inventory Service Metrics Target, Order Service Metrics Target, Payment Service Metrics Target (+11 more)

### Community 8 - "Service Bootstraps"
Cohesion: 0.20
Nodes (6): ApiGatewayApplication, InventoryServiceApplication, OrderServiceApplication, ProductCatalogApplication, org.springframework.boot.autoconfigure.SpringBootApplication, org.springframework.scheduling.annotation.EnableScheduling

### Community 9 - "Frontend Compiler Settings"
Cohesion: 0.15
Nodes (12): compilerOptions, isolatedModules, jsx, lib, module, moduleResolution, noEmit, skipLibCheck (+4 more)

### Community 10 - "Graphify Tooling"
Cohesion: 0.22
Nodes (9): URL Ingestion, Graph Exports, Cross-Repository Graph Merge, Post-Commit Graph Hook, Graph Query, Whisper Transcription, Incremental Graph Update, Graphify (+1 more)

### Community 11 - "Node TypeScript Settings"
Cohesion: 0.29
Nodes (6): compilerOptions, composite, module, moduleResolution, skipLibCheck, include

### Community 12 - "Commerce Service Architecture"
Cohesion: 0.53
Nodes (6): API Gateway Routes, API Gateway, Inventory Service, orders.created.v1 Kafka Event, Order Service, Event-Driven Commerce Platform

### Community 13 - "Docker Monitoring Stack"
Cohesion: 0.33
Nodes (6): Local Infrastructure Stack, Kafka Runtime, Observability Stack, Inventory Service Configuration, EventFlow Commerce Dashboard, Local Monitoring

### Community 14 - "Gradle Wrapper"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 16 - "React Operations Frontend"
Cohesion: 0.50
Nodes (4): Commerce Platform Operations Console, Frontend Main Entry Module, Commerce Platform Frontend, React TypeScript Vite

## Knowledge Gaps
- **67 isolated node(s):** `name`, `private`, `version`, `type`, `dev` (+62 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 129 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **6 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ProductRepository` connect `Product Catalog API` to `Inventory API`?**
  _High betweenness centrality (0.055) - this node is a cross-community bridge._
- **Why does `Order` connect `Inventory API` to `Inventory Domain Model`?**
  _High betweenness centrality (0.038) - this node is a cross-community bridge._
- **What connects `name`, `private`, `version` to the rest of the system?**
  _67 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Inventory API` be split into smaller, more focused modules?**
  _Cohesion score 0.07419712070874862 - nodes in this community are weakly interconnected._
- **Should `Frontend Dependencies` be split into smaller, more focused modules?**
  _Cohesion score 0.06090808416389812 - nodes in this community are weakly interconnected._
- **Should `Kafka Event Contracts` be split into smaller, more focused modules?**
  _Cohesion score 0.12317073170731707 - nodes in this community are weakly interconnected._
- **Should `Payment Checkout API` be split into smaller, more focused modules?**
  _Cohesion score 0.11827956989247312 - nodes in this community are weakly interconnected._