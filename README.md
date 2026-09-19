# SmartTask API

Java 17 / Spring Boot task management REST API. Layered controller → service → repository design, MySQL persistence, input validation, JUnit and Mockito tests, and generated OpenAPI docs. No frontend or login system.

## Requirements

- JDK 17 or newer, Maven 3.9+, MySQL 8+
- Create a local database and account (example; change the password for your machine):

```sql
CREATE DATABASE smarttask;
CREATE USER 'smarttask'@'localhost' IDENTIFIED BY 'choose-a-local-password';
GRANT ALL PRIVILEGES ON smarttask.* TO 'smarttask'@'localhost';
```

Set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` as environment variables. `DB_URL` defaults to `jdbc:mysql://localhost:3306/smarttask`; the default username and password are both `smarttask` for local development only. The example SQL above needs `DB_PASSWORD` set to the chosen password. Never commit credentials.

```bash
mvn clean verify
mvn spring-boot:run
```

Open `http://localhost:8080/swagger-ui.html` for interactive API documentation and `http://localhost:8080/v3/api-docs` for OpenAPI JSON. Tests mock the repository and do not require MySQL. `mvn verify` writes a local coverage report to `target/site/jacoco/index.html`.

## Try the API

```bash
curl -i -X POST http://localhost:8080/api/tasks -H 'Content-Type: application/json' -d '{"title":"Fix API","description":"Add tests","priority":"HIGH"}'
curl 'http://localhost:8080/api/tasks?priority=HIGH&status=TODO'
curl -X PATCH http://localhost:8080/api/tasks/1/status -H 'Content-Type: application/json' -d '{"status":"IN_PROGRESS"}'
```

| Method | Path | Result |
| --- | --- | --- |
| POST | `/api/tasks` | 201, new task and Location header |
| GET | `/api/tasks` | 200, list; optional `status` and `priority` filters |
| GET | `/api/tasks/{id}` | 200 or 404 |
| PUT | `/api/tasks/{id}` | 200 or 404; replaces title, description, priority; preserves status |
| PATCH | `/api/tasks/{id}/status` | 200 or 404 |
| DELETE | `/api/tasks/{id}` | 204 or 404 |

Priorities: `LOW`, `MEDIUM`, `HIGH`. Statuses: `TODO`, `IN_PROGRESS`, `COMPLETED`. A new task starts in `TODO`. Bad JSON or invalid input receives a 400 error with `status`, `message`, and `timestamp`; missing IDs receive 404. Status changes allow any enum value; no restrictive transition policy is implied.

See [architecture](docs/architecture.md), [testing](docs/testing.md), and [AI assistance](docs/ai-assisted-development.md). The [Postman collection](docs/SmartTask.postman_collection.json) provides example requests.

## Verification

Verified locally on Windows 11 with Java 22, Maven 3.9.11, and MySQL 8.0.36:

- `mvn clean verify` completed successfully with 22 tests, 0 failures, 0 errors, and 0 skipped.
- JaCoCo measured 90.91% line coverage (60 of 66 lines) and 100% branch coverage (4 of 4 branches).
- Spring Boot started on port 8080 and exposed all six task operations through Swagger UI.
- `POST /api/tasks` returned `201 Created`, applied the default `TODO` status, and returned a `Location` header.
- The created task was confirmed in the MySQL `tasks` table, verifying JPA/Hibernate persistence.

## Honest scope

This is a portfolio API for local execution. The public GitHub URL hosts the **source code**, not a deployed server. Production deployments would need controlled schema migrations, credentials management, authentication, and operational monitoring. Do not claim measured coverage or runtime performance until verified.
