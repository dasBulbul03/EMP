## Employee Management System (Spring Boot)

This is a simple **Employee Management** web application built with **Java 17** and **Spring Boot 3**.
It exposes a REST API and a minimal Thymeleaf-based UI for basic CRUD operations on employees.

### Tech stack

- **Backend**: Spring Boot 3 (Web, Data JPA, Validation)
- **Database**: H2 in-memory (default), example MySQL profile (commented in `application.yml`)
- **View**: Thymeleaf + Bootstrap (via CDN)
- **Build**: Maven
- **Tests**: JUnit 5, Mockito

### Requirements

- Java 17+
- Maven 3.8+

### Running the application

From the project root:

```bash
mvn -DskipTests=false clean spring-boot:run
```

The app will start on `http://localhost:8080`.

- **UI (Thymeleaf)**: `http://localhost:8080/employees`
- **REST API base path**: `http://localhost:8080/api/employees`
- **H2 console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:emsdb`, user: `sa`, empty password)

### Running tests

```bash
mvn test
```

### Building the JAR

```bash
mvn clean package
```

The JAR will be created as `target/ems-0.0.1-SNAPSHOT.jar`.

### Docker

Build the Docker image:

```bash
docker build -t ems-app .
```

Run the container:

```bash
docker run -p 8080:8080 ems-app
```

Then access:

- UI: `http://localhost:8080/employees`
- API: `http://localhost:8080/api/employees`

### Lombok

The project includes Lombok as an optional dependency in `pom.xml`, but the main classes (entities and DTOs) have explicit getters and setters so Lombok is **not required**. If you want to use Lombok in additional classes, enable annotation processing in your IDE and install the Lombok plugin if needed.

### Example cURL commands

**Create employee (POST)**

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

**Get list of employees (GET with pagination, sorting, and search)**

```bash
curl "http://localhost:8080/api/employees?page=0&size=5&sort=lastName,asc&search=john"
```

**Get a single employee by ID**

```bash
curl "http://localhost:8080/api/employees/1"
```

**Update employee (PUT)**

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

**Delete employee (DELETE)**

```bash
curl -X DELETE "http://localhost:8080/api/employees/1"
```

### Expected startup logs (excerpt)

When you run `mvn spring-boot:run`, you should see lines similar to:

```text
...  INFO  ... o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http)
...  INFO  ... com.example.ems.EmsApplication          : Started EmsApplication in X.XXX seconds
...  INFO  ... com.example.ems.data.SampleDataLoader   : Inserted 10 sample employees
```

### Sample API responses (examples)

**GET `/api/employees`**

```json
{
  "content": [
    {
      "id": 1,
      "firstName": "Alice",
      "lastName": "Johnson",
      "email": "alice.johnson@example.com",
      "jobTitle": "Developer",
      "department": "Engineering",
      "salary": 80000.00,
      "hireDate": "2022-09-01"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 10,
  "totalPages": 1,
  "last": true
}
```

**Error response example (duplicate email)**

```json
{
  "timestamp": "2025-11-26T10:15:30.123",
  "status": 409,
  "error": "Conflict",
  "message": "Employee with email already exists: test.user@example.com",
  "path": "/api/employees",
  "validationErrors": null
}
```


