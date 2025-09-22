# TripTally

TripTally is a Java and Spring Boot–powered student project that makes it easy to plan, budget, and track travel expenses across multiple trips and currencies. With TripTally you can:

- Define trips with start/end dates, destinations, home-currency and local-currency budgets.

- Log daily expenses and wishlist items.

- Automatically convert local spending back into your home currency using *FX rates.

- Generate summary reports to stay on budget.

<sub><sup>*FX rates do not rely on an external API. Please update the rates to the value of market when used.
See `scr/main/org/example/eindopdracht/travel/currencyRates/FxSnapshotLoader` for the rates that are being used in this project.</sup></sub>

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

## Getting Started

Prerequisites:

- Java 21
- Maven
- PostgreSQL (local or Docker)

### Setup:

Clone the repository:

Using HTTPS:
```bash
git clone https://github.com/your-username/TripTally.git
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
spring.application.name=TripTally
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/triptally_db
spring.datasource.username=postgres spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update spring.jpa.show-sql=true
spring.mvc.hiddenmethod.filter.enabled=true
```

### Optional: load secrets from a local env file (gitignored)

```properties
spring.config.import=optional:file:SUPERADMIN_SECRET.env[.properties]
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
  - GET /api/v1/users/{userId}/viewing-access?loggedInUserId={selfId} → view user with access rules (ROLE_USER)
  - POST /api/v1/users/{userId}/viewing-access?loggedInUserId={selfId} → grant mutual viewing access (ROLE_USER)
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
  - POST /api/v1/images/upload/{userId} → upload image (ROLE_USER, self)
  - GET /api/v1/images/download/{imageId} → download image (ROLE_USER)
  - DELETE /api/v1/images/delete-image/{userId} → delete image (ROLE_USER, self)

- Admin (ROLE_ADMIN)
  - GET /api/v1/admin/{id}?adminId={selfId} → fetch a user
  - GET /api/v1/admin?adminId={selfId} → list users
  - PUT /api/v1/admin/{id} → update user (policy enforced)
  - DELETE /api/v1/admin/{id}?adminId={selfId} → delete user (policy enforced)

- Super Admin (ROLE_SUPERADMIN; header X-SUPERADMIN-SECRET for certain ops)
  - POST /api/v1/superadmin/create-super-admin → bootstrap super admin (requires header)
  - POST /api/v1/superadmin/admins?superAdminId={selfId} → create admin
  - GET /api/v1/superadmin/users?superAdminId={selfId} → list all users
  - GET /api/v1/superadmin/users/{id}?superAdminId={selfId} → get user
  - PUT /api/v1/superadmin/users/{id}?superAdminId={selfId} → update user (optional override via header)
  - DELETE /api/v1/superadmin/admins/{id}?superAdminId={selfId} → delete admin (optional override via header)
```

Notes:
- Most routes require Authorization: Bearer <token>.
- “Self” means the authenticated user must match the path/query id as indicated.
- For trip/expense/wishlist item routes, the resource must belong to the specified trip where noted.


## Development

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Production

```bash
java -jar target/TripTally-0.0.1.jar --spring.profiles.active=prod
```

## Environment & Secrets

Use environment variables (recommended):

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/triptally_db
export PRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=supersecret
export APP_SECURITY_JWT_SECRET=ultra_secret
```

Optional .env file (gitignored):
```dotenv
SPRING_DATASOURCE_PASSWORD=supersecret APP_SECURITY_JWT_SECRET=ultra_secret
```

## Testing

```bash
mvn test mvn verify mvn jacoco:report
```

## Docker (Optional)

```bash
docker build -t triptally:latest . docker run -p 8080:8080
-e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/triptally_db
-e SPRING_DATASOURCE_USERNAME=postgres
-e SPRING_DATASOURCE_PASSWORD=supersecret
-e APP_SECURITY_JWT_SECRET=ultra_secret
triptally:latest
```

## Common Issues

- DB connection fails: verify URL, credentials, and that PostgreSQL is running
- Port 8080 busy: run with `server.port=9090`
- 401/403: ensure auth header/token if security is enabled