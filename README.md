# 🛒 Online Shopping Application

A full-stack Java Spring Boot application that serves as a backend for an online shopping platform. It manages products, users, orders, and supports basic configurations for environments and deployment.

---

## 🚀 Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA (MongoDB or relational DB configurable)**
- **Maven** – Project build and dependency management
- **Thymeleaf** (if used in templates directory)
- **Docker** (optional, for containerization)
- **Azure** (optional, for deployment)
- **JUnit** – For unit testing
- **Git & GitHub** – For version control and collaboration

---

---

## 📄 Key Components

- `OnlineShoppingApplication.java`: Main entry point of the Spring Boot app.
- `controller/`: Handles HTTP requests (e.g., adding products, placing orders).
- `model/`: Contains Java classes that map to database entities (e.g., `Product`, `User`, `Order`).
- `service/`: Contains business logic methods (e.g., validating order totals, applying discounts).
- `repository/`: Interfaces extending `JpaRepository` or `MongoRepository` for DB operations.
- `config/`: Holds configuration classes such as CORS config, Swagger config, etc.
- `application.properties`: Shared properties across environments.
- `application.azure.properties`: Properties for Azure-specific deployments.

---

## 📦 Build & Run

### 🛠️ Build the project

```bash
./mvnw clean install


