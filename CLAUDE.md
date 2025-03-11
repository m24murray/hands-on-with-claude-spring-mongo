# Project Reference

## Common Commands
- Build: `./gradlew build`
- Run tests: `./gradlew test`
- Run specific test: `./gradlew test --tests "com.example.userservice.user.controller.UserControllerTest"`
- Run application: `./gradlew bootRun`

## Project Structure
- Controller layer: `src/main/java/com/example/userservice/user/controller/`
- Service layer: `src/main/java/com/example/userservice/user/service/`
- Repository layer: `src/main/java/com/example/userservice/user/repository/`
- Model classes: `src/main/java/com/example/userservice/user/model/`
- Exception handling: `src/main/java/com/example/userservice/common/exception/`
- Configuration: `src/main/java/com/example/userservice/config/`

## Coding Conventions
- Use Lombok annotations to reduce boilerplate
- Controller methods return ResponseEntity<?>
- Service methods throw specific exceptions rather than returning null
- Repository methods follow Spring Data naming conventions
- Tests follow given/when/then pattern
- OpenAPI annotations on all controller methods

## Key Files
- User model: `src/main/java/com/example/userservice/user/model/User.java`
- Main controller: `src/main/java/com/example/userservice/user/controller/UserController.java`
- Service interface: `src/main/java/com/example/userservice/user/service/UserService.java`
- Main config: `src/main/resources/application.yml`
- Logging config: `src/main/resources/logback-spring.xml`

## API Endpoints
- Health check: `GET /api/v1/health`
- Get user by ID: `GET /api/v1/users/{id}`
- Get all users: `GET /api/v1/users`
- Search user by email: `GET /api/v1/users?email=example@example.com`
- Create user: `POST /api/v1/users`
- Update user: `PUT /api/v1/users/{id}`
- Partial update: `PATCH /api/v1/users/{id}`
- Delete user: `DELETE /api/v1/users/{id}`

## Documentation
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/api-docs`

## Database Access
- MongoDB running on localhost:27017
- Database name: userdb