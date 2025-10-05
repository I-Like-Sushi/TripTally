# TripTally

<img src="./src/main/resources/static/TripTally.png" alt="TripTally logo" width="500" height="400">

TripTally is a Java and Spring Boot–powered student project that makes it easy to plan, budget, and track travel expenses across multiple trips and currencies. With TripTally you can:

- Define trips with start/end dates, destinations, home-currency and local-currency budgets.

- Log daily expenses and wishlist items.

- Automatically convert local spending back into your home currency using *FX rates.

- Generate summary reports to stay on budget.

<sub><sup>*FX rates do not rely on an external API. Please update the rates to the value of market when used.
See `scr/main/org/example/triptally/travel/currencyRates/FxSnapshotLoader` for the rates that are being used in this project.</sup></sub>

---

## Why this project?

- Practice building a layered Spring Boot application
- Work with databases and JPA
- Explore REST API design, validation, and testing

---

## Tech Stack

- Java 21 or higher
- Spring Boot
- Spring Data JPA (PostgreSQL)
- Apache Maven 3.9.9
- Docker (optional)
- JUnit + Mockito (tests)

---

## Project Dependencies

### Spring Boot Starters
- **spring-boot-starter-web** – Build RESTful web applications with Spring MVC
- **spring-boot-starter-data-jpa** – JPA and Hibernate integration for database access
- **spring-boot-starter-validation** – Bean validation with Hibernate Validator
- **spring-boot-starter-security** – Authentication and authorization support
- **spring-boot-starter-actuator** – Production-ready monitoring and management endpoints
- **spring-boot-starter-webflux** – Reactive programming with Spring WebFlux

### Database
- **org.postgresql:postgresql:42.7.4** – PostgreSQL JDBC driver (runtime)

### JWT (JSON Web Tokens)
- **io.jsonwebtoken:jjwt-api:0.12.7** – JWT API
- **io.jsonwebtoken:jjwt-impl:0.12.7** (runtime) – JWT implementation
- **io.jsonwebtoken:jjwt-jackson:0.12.7** (runtime) – JSON serialization/deserialization for JWT

### Testing
- **spring-boot-starter-test** – Spring Boot testing utilities
- **spring-security-test** – Security testing support
- **org.junit.jupiter:junit-jupiter:5.9.2** – JUnit 5 testing framework
- **org.mockito:mockito-junit-jupiter:4.8.0** – Mockito integration with JUnit 5


---

## Getting Started

### Setup:

Clone the repository:

Using HTTPS:
```bash
git clone https://github.com/I-Like-Sushi/TripTally.git
```

Or Using SSH:
```bash
git@github.com:I-Like-Sushi/TripTally.git
```

Then:

```bash
cd TripTally
```


## Configure (application.properties)

Create or edit `src/main/resources/application.properties`:

```properties
# Database details
spring.application.name=TripTally
spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Helps with deleting from SQL
spring.mvc.hiddenmethod.filter.enabled=true

# Debugging
logging.level.org.example.userdemo2.security.JwtAuthFilter=DEBUG

spring.jpa.generate-ddl=true

spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true

# datasource PostgreSQl
spring.sql.init.platform=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# jpa
spring.jpa.database=postgresql
```

### Optional: load secrets from a local env file (gitignored)

```properties
spring.config.import=optional:file:SUPERADMIN_SECRET.env[.properties], \
  optional:file:DB_NAME.env[.properties],\
  optional:file:DB_USERNAME.env[.properties],\
  optional:file:DB_PASSWORD.env[.properties]

```

## Domain-specific example

```properties
fx.snapshot-date=2025-09-17
```

## Run

```bash
mvn spring-boot:run
```
Or:
```bash
mvn clean package && java -jar target/TripTally-0.0.1.jar
```


## API Overview

```text
## API Overview

- Auth
  - POST /api/v1/auth/login → authenticate and receive JWT
  - GET /api/v1/auth/me → current user profile (requires auth)

- Users
  - POST /api/v1/users → register a new user
  - PUT /api/v1/users/{id} → update own user (ROLE_USER)
  - DELETE /api/v1/users/{id} → delete own user (ROLE_USER)
  - GET /api/v1/users/{userId}/viewing-access/{targetUserId} → view user with access rules (ROLE_USER)
  - POST /api/v1/users/{userId}/viewing-access/{targetUserId} → grant mutual viewing access (ROLE_USER)
  - DELETE /api/v1/users/{userId}/viewing-access/{targetUserId} → revoke mutual viewing access (ROLE_USER)

- Trips (scoped to user)
  - POST /api/v1/users/{userId}/trips → create trip (ROLE_USER, self)
  - GET /api/v1/users/{userId}/trips/{tripId} → get trip (ROLE_USER, has viewing access to target user; targetId provided in request body)
  - PUT /api/v1/users/{userId}/trips/{tripId} → update trip (ROLE_USER, self)
  - DELETE /api/v1/users/{userId}/trips/{tripId} → delete trip (ROLE_USER, self)

- Expenses (scoped to user and trip)
  - POST /api/v1/users/{userId}/trips/{tripId}/expenses → create expense (ROLE_USER, self)
  - GET /api/v1/users/{userId}/trips/{tripId}/expenses/{expenseId} → get expense (ROLE_USER, has viewing access; targetId provided in request body; must belong to trip)
  - PUT /api/v1/users/{userId}/trips/{tripId}/expenses/{expenseId} → update expense (ROLE_USER, self; must belong to trip)
  - DELETE /api/v1/users/{userId}/trips/{tripId}/expenses/{expenseId} → delete expense (ROLE_USER, self; must belong to trip)

- Wishlist Items (scoped to user and trip)
  - POST /api/v1/users/{userId}/trips/{tripId}/wishlist-item → create wishlist item (ROLE_USER, self)
  - GET /api/v1/users/{userId}/trips/{tripId}/wishlist-item/{wishlistItemId} → get wishlist item (ROLE_USER, self; must belong to trip)
  - PUT /api/v1/users/{userId}/trips/{tripId}/wishlist-item/{wishlistItemId} → update wishlist item (ROLE_USER, self; must belong to trip)
  - DELETE /api/v1/users/{userId}/trips/{tripId}/wishlist-item/{wishlistItemId} → delete wishlist item (ROLE_USER, self; must belong to trip)

- Images (user-scoped)
  - POST /api/v1/users/{userId}/images → upload image (ROLE_USER, self)
  - GET /api/v1/users/{userId}/images/{ImageId} → download image (ROLE_USER)
  - DELETE /api/v1/users/{userId}/images/{ImageId} → delete image (ROLE_USER, self)

- Admin (ROLE_ADMIN)
  - GET /api/v1/admin/{adminId} → list users
  - GET /api/v1/admin/{adminId}/{targetId} → fetch a user
  - PUT /api/v1/admin/{adminId}/{targetId} → update user (policy enforced)
  - PATCH /api/v1/admin/{adminId}/{targetId} → set whether user is active or not (policy enforced)
  - DELETE /api/v1/admin/{adminId}/{targetId} → delete user (policy enforced)

- Super Admin (ROLE_SUPERADMIN; header X-SUPERADMIN-SECRET for certain ops)
  - POST /api/v1/superadmin → bootstrap super admin (requires header)
  - POST /api/v1/superadmin/{superAdminId} → create admin
  - GET /api/v1/superadmin/{superAdminId}/users → list all users
  - GET /api/v1/superadmin/{superAdminId}/users/{targetId} → get user
  - PUT /api/v1/superadmin/{superAdminId}/users/{targetId} → update user (optional override via header)
  - DELETE /api/v1/superadmin/{superAdminId}/users/{targetId} → delete admin (optional override via header)
```

Notes:
- Most routes require Authorization: Bearer <token>.
- “Self” means the authenticated user must match the path/query id as indicated.
- For trip/expense/wishlist item routes, the resource must belong to the specified trip where noted.

## API Example

### Sending a request:

Create a new user: <br/>
Method: POST <br/>
Content-Type: application/json <br/>
URI: {{host}}/api/v1/users
```json
{
  "username": "sushiFan92",
  "firstName": "Aiko",
  "lastName": "Tanaka",
  "email": "aiko.tanaka@example.com",
  "password": "Pasword_55897",
  "bio": "Traveler, foodie, and backend tinkerer who loves exploring new cities and debugging old code.",
  "dateOfBirth": "1992-07-15",
  "gender": "female"
}
```

### Receiving a response:

Location: /users/5001645532 <br/>
Content-Type: application/json

```json
{
    "id": 5001645532,
    "username": "sushiFan92",
    "firstName": "Aiko",
    "lastName": "Tanaka",
    "email": "aiko.tanaka@example.com",
    "dateOfBirth": "1992-07-15",
    "accountCreatedAt": "2025-10-05 11:28:49",
    "roles": [
        "ROLE_USER"
    ],
    "gender": "female",
    "bio": "Traveler, foodie, and backend tinkerer who loves exploring new cities and debugging old code.",
    "enabled": true
}
```

## Development

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Production

```bash
java -jar target/TripTally-0.0.1.jar --spring.profiles.active=prod
```

## Environment & Secrets

.env file (gitignored), create your own passwords:
```dotenv
SUPERADMIN_SECRET=supersecret
DB_NAME=triptally_db
DB_USERNAME=even_more_secret
DB_PASSWORD=secrety_secret
```

Please note that you must create these `.env` files inside your `src` directory. If you do not wish to use `.env` files, you may delete the `spring.config.import` property from the `application.properties` file.

## Docker (Optional)

```bash
docker build -t triptally:latest . docker run -p 8080:8080
-e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/triptally_db
-e SPRING_DATASOURCE_USERNAME=even_more_secret
-e SPRING_DATASOURCE_PASSWORD=secrety_secret
triptally:latest
```

## Common Issues

- DB connection fails: verify URL, credentials, and that PostgreSQL is running
- Port 8080 busy: run with `server.port=9090`
- 401/403: ensure auth header/token if security is enabled