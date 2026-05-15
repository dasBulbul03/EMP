# EMP

A Spring Boot–based Employee Management System with full CRUD operations, REST APIs, a small Thymeleaf UI, pagination, sorting, validation, H2 support, unit tests, and a Dockerfile for containerized deployment.

## Employee Management System (Spring Boot)

This repository contains a simple Employee Management web application built with Java and Spring Boot. It provides a web UI and a REST API for performing CRUD operations on employees.

### Tech summary

- Java: 17+ (project property is set to 21 in `pom.xml` but the app runs on Java 17+)
- Spring Boot: 3.5.x
- Build: Maven
- Default DB: H2 (in-memory); MySQL profile available for production use

### Quick start (development)

Build and run with Maven:

```powershell
mvn -DskipTests=false clean spring-boot:run
```

Open in your browser:

- UI: `http://localhost:8080/employees`
- REST API base: `http://localhost:8080/api/employees`
- H2 console: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:emsdb`, user `sa`, empty password)

Build the runnable JAR:

```powershell
mvn clean package
```

The artifact will be available at `target/ems-0.0.1-SNAPSHOT.jar`.

### Docker

Build the image (project root):

```powershell
docker build -t ems-app .
```

Run the container:

```powershell
docker run --rm -p 8080:8080 ems-app
```

Notes:

- The included `Dockerfile` uses a multi-stage build: a Maven builder stage and an Eclipse Temurin runtime stage (Java 21 images). Adjust to your environment if needed.

### Tests

```powershell
mvn test
```

### API examples

Create an employee (POST):

```bash
curl -X POST "http://localhost:8080/api/employees" \
  -H "Content-Type: application/json" \
  -d '{
        "firstName": "Test",
        "lastName": "User",
        "email": "test.user@example.com",
        "jobTitle": "Engineer",
        "department": "Engineering",
        "salary": 75000,
        "hireDate": "2022-01-15"
      }'
```

Get a paginated list of employees:

```bash
curl "http://localhost:8080/api/employees?page=0&size=5&sort=lastName,asc&search=john"
```

Get a single employee:

```bash
curl "http://localhost:8080/api/employees/1"
```

Update an employee (PUT):

```bash
curl -X PUT "http://localhost:8080/api/employees/1" \
  -H "Content-Type: application/json" \
  -d '{
        "firstName": "Updated",
        "lastName": "User",
        "email": "updated.user@example.com",
        "jobTitle": "Senior Engineer",
        "department": "Engineering",
        "salary": 90000,
        "hireDate": "2020-05-01"
      }'
```

Delete an employee:

```bash
curl -X DELETE "http://localhost:8080/api/employees/1"
```

### Expected startup logs

When the application starts you should see entries similar to:

```text
Tomcat started on port 8080 (http)
Started EmsApplication in X.XXX seconds
Inserted 10 sample employees
```

### CI/CD & Deployment (pipeline notes)

- The repository includes a GitHub Actions workflow at `.github/workflows/ci-cd.yml` that:
  - runs tests and builds the JAR,
  - builds and pushes the Docker image to Docker Hub, and
  - deploys to Kubernetes (on `main`) and runs the smoke test.

- Required repository secrets (GitHub → Settings → Secrets and variables → Actions):
  - `DOCKERHUB_USERNAME` — your Docker Hub username
  - `DOCKERHUB_TOKEN` — Docker Hub access token (preferred)
  - `KUBE_CONFIG_DATA` — base64-encoded kubeconfig content (generate with command below)

How to generate `KUBE_CONFIG_DATA` (PowerShell):

```powershell
# PowerShell (copy-paste the output into the GitHub secret value)
[Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes((Get-Content $env:USERPROFILE + '\\.kube\\config' -Raw)))
```

Notes:

- `KUBE_CONFIG_DATA` will be decoded by the workflow and written to `$HOME/.kube/config` so `kubectl` can apply manifests.
- For cloud clusters (GKE/AKS/EKS) it's more secure to use provider-specific GitHub Actions for authentication — tell me which cloud and I will update the workflow.

### More info

- Lombok: optional — the code works without Lombok, but Lombok is included as an optional developer convenience.
- See `application.yml` for profile and datasource configuration.

If you'd like, I can walk you through adding the GitHub secrets or update the README further.
