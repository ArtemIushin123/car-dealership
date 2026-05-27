# Car Dealership System

Backend system for a multi-brand car dealership built with microservice architecture.

## Overview

The system consists of two independent microservices that handle car orders and warehouse management. Services communicate synchronously via gRPC and asynchronously via RabbitMQ message broker.

## Architecture

**order-service** — handles client-facing operations:
- Car orders (stock and custom configuration)
- Test drive requests
- Order lifecycle management
- Authentication and authorization

**storage-service** — handles warehouse operations:
- Car and parts catalog
- Assembly orders
- Stock management
- gRPC server for car data

## Tech Stack

- **Java 21**
- **Spring Boot 3.2**
- **Spring Security + Keycloak** — authentication and role-based authorization
- **Spring Data JPA + PostgreSQL** — data persistence
- **Liquibase** — database migrations
- **RabbitMQ** — asynchronous messaging between services
- **gRPC + Protobuf** — synchronous inter-service communication
- **Docker + Docker Compose** — containerization
- **Gradle** — build system
- **Swagger/OpenAPI** — API documentation

## Key Features

- JWT-based authentication via Keycloak
- Role-based access control (USER, MANAGER, WAREHOUSE_ADMIN, ADMIN)
- Outbox Pattern for reliable message delivery
- Idempotent message processing
- Car configurator with component compatibility validation
- gRPC with timeout and 503 fallback on service unavailability
