# Product & Category Management Service

## 1. Overview

This service provides CRUD operations for product categories and products. It enforces role-based access control (ADMIN for mutations, public for reads) and follows best practices for data modeling, ORM, validation, error handling, and documentation.

**Key Features:**

*   **Categories:** Create, delete, update, list
*   **Products:** Create, delete, update, list (under a specific category)
*   **Security:** Basic Authentication; ADMIN role required for create/update/delete
*   **Validation:** Jakarta Bean Validation on request DTOs
*   **Error Handling:** Global exception handling with clear, safe responses
*   **Documentation:** Swagger/OpenAPI integration (Springdoc)

---

## 2. Technology Stack

| Layer              | Technology                            |
| ------------------ | ------------------------------------- |
| Language           | Java 17                               |
| Framework          | Spring Boot 3.1.5                     |
| ORM                | Hibernate (via Spring Data JPA)       |
| Database           | PostgreSQL                            |
| Authentication     | HTTP Basic Auth (Spring Security)     |
| Validation         | Jakarta Bean Validation (JSR-380)     |
| Documentation      | Springdoc OpenAPI v2.2.0                |
| Build & Dependency | Maven                                 |
| Testing            | JUnit 5, Mockito                      |

---

## 3. Prerequisites

*   Java 17 JDK
*   Apache Maven 3.6+
*   PostgreSQL (or your preferred DBMS, but PostgreSQL is configured by default)
*   An IDE (like IntelliJ IDEA, Eclipse, VS Code with Java extensions)

---

## 4. Setup & Running the Application

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd springfinalproject
    ```

2.  **Database Configuration:**
    *   Ensure PostgreSQL is installed and running.
    *   Create a database for the application (e.g., `product_category_db`).
    *   Open `src/main/resources/application.yml`.
    *   Update the following datasource properties with your database details:
        ```yaml
        spring:
          datasource:
            url: jdbc:postgresql://localhost:5432/your_database_name # Replace your_database_name
            username: your_db_username # Replace your_db_username
            password: your_db_password # Replace your_db_password
        ```

3.  **Build the project:**
    This will download dependencies and compile the code.
    ```bash
    mvn clean install
    ```

4.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the `Application.java` class from your IDE.

    The application will start on `http://localhost:8080` by default.

---

## 5. API Endpoints & Documentation

Once the application is running, API documentation (Swagger UI) is available at:

*   `http://localhost:8080/swagger-ui.html`

Key API groups:
*   **Category Management:** `/api/categories`
*   **Product Management:** `/api/categories/{categoryId}/products` and `/api/products`

Refer to the Swagger UI for detailed information on all endpoints, request/response formats, and to try out the APIs.

---

## 6. Security

*   **Basic Authentication** is used.
*   **Read operations (GET)** on `/api/**` are public.
*   **Mutation operations (POST, PUT, DELETE)** on `/api/**` require the `ADMIN` role.
*   Default credentials (configured in `SecurityConfig.java`):
    *   Username: `admin`
    *   Password: `adminpass` (this is BCrypt encoded in the configuration)

    You will need to provide these credentials via Basic Auth headers for protected endpoints.

---

## 7. Project Structure

```
src/main/java/com/example/app/
├── Application.java            # Spring Boot entry point
├── config/
│   ├── SecurityConfig.java     # Basic Auth setup
│   └── SwaggerConfig.java      # OpenAPI configuration
├── controller/
│   ├── CategoryController.java
│   └── ProductController.java
├── dto/
│   ├── CategoryDto.java
│   └── ProductDto.java
├── entity/
│   ├── Category.java
│   └── Product.java
├── exception/
│   ├── ApiError.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository/
│   ├── CategoryRepository.java
│   └── ProductRepository.java
├── service/
│   ├── CategoryService.java
│   └── ProductService.java
└── validation/
    └── CustomValidators.java   # Placeholder for custom validators
src/main/resources/
├── application.yml             # Application configuration
└── static/                     # For static assets (if any)
└── templates/                  # For view templates (if any)
pom.xml                         # Maven project configuration
```

--- 