# Ticker API

REST service for user registration, authentication, and fetching/saving historical stock prices from an external API.

---

## Tech Stack

* Java 21
* Spring Boot
* Spring Security (JWT)
* PostgreSQL
* Liquibase
* Docker / Docker Compose
* OpenAPI (Swagger)

---

## Features

* User registration and login (JWT-based authentication)
* Fetch historical ticker prices for a given period from an external API
* Save user-specific price data
* Retrieve previously saved prices

---

## Requirements

* Docker + Docker Compose

or

* JDK 21
* Maven

---

## Run with Docker (recommended)

### 1. Prepare `.env`

Create a `.env` file in the project root (not committed to git):

```env
POSTGRES_DB=tickerapi
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

JWT_SIGNING_KEY=change-me-very-secret
POLYGON_API_KEY=your-api-key
```

See `.env.example` for reference.

---

### 2. Build the jar

```bash
mvn package
```

(or via IDE: Maven → Lifecycle → package)

---

### 3. Start containers

```bash
docker compose up --build
```

After startup:

* API: [http://localhost:8080](http://localhost:8080)
* Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

### 4. Stop

```bash
docker compose down
```

Database data is preserved using a Docker volume.

---

## Run locally without Docker

1. Create a PostgreSQL database
2. Set environment variables:

    * `POSTGRES_DB`
    * `POSTGRES_USER`
    * `POSTGRES_PASSWORD`
    * `JWT_SIGNING_KEY`
    * `POLYGON_API_KEY`
3. Run the application:

```bash
mvn spring-boot:run
```

Liquibase migrations are applied automatically on startup.

---

## Tests

* Integration tests use Testcontainers and WireMock
* Tests run with the `test` Spring profile
* Test configuration: `src/test/resources/application-test.yml`

---

## Notes

* `.env` and `logs/` are not committed to git
* Secrets are never logged
* Liquibase migrations run automatically on application startup
