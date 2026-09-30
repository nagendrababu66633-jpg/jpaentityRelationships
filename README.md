# Mobile Backend - Spring Boot

A RESTful backend application developed using **Java, Spring Boot, Spring Data JPA, Hibernate, PostgreSQL, and Spring Security with JWT**.

## Technologies

* Java
* Spring Boot
* Spring Data JPA / Hibernate
* PostgreSQL
* Spring Security
* JWT
* Maven
* Postman

## Features

* User Registration & Login
* JWT Authentication & Authorization
* Role-based Access (`USER`, `ADMIN`, `DRIVER`)
* User & Profile Management
* Address Management
* Category & Product CRUD
* Order Management
* Transaction Management
* Global Exception Handling

## Architecture

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
PostgreSQL
```

## Main APIs

```text
POST   /api/auth/register

GET    /api/v1/products
GET    /api/v1/products/{id}
POST   /api/v1/products
PUT    /api/v1/products/{id}
DELETE /api/v1/products/{id}

GET    /api/v1/categories
```

## Database

```text
PostgreSQL
Database: mobilebackend
```

## Run

```bash
mvn clean install
mvn spring-boot:run
```

Application URL:

```text
http://localhost:8080
```

## Testing

APIs are tested using **Postman**.
