# Java Spring REST API Implementation Guide

This document outlines the step-by-step implementation of the API specified in `spec.md`. Each section represents a single development iteration, following Test-Driven Development principles.

## Important Notes

- After completing each iteration, we will stop to manually test and evaluate the implementation before proceeding to the next iteration.
- Each iteration must be fully tested and functional before moving on.
- You should keep the project's README file up to date at all times.

## Cost-Effective Prompting Guidelines

- Be specific with requests, including exact file paths and line numbers when applicable
- Group related changes in a single prompt (e.g., "Implement User model and repository" rather than two separate prompts)
- Use short, focused prompts that specify exactly what you need
- For search operations, use specific patterns rather than open-ended searches
- Provide necessary context upfront rather than through multiple back-and-forth exchanges
- Skip explanations of standard patterns when you already understand them
- Use the sample prompts provided in each iteration as templates
- Reference the Common Commands section at the end rather than asking about build/test commands

### Creating a CLAUDE.md File

For even more efficiency, create a CLAUDE.md file in the project root with:
- Common commands and their explanations
- Project structure overview
- Coding conventions specific to this project
- Links to relevant documentation

This file will be automatically loaded into Claude's context in each session, reducing the need to re-explain project specifics.

## Iteration 1: Project Setup and Basic Structure
**Status:**  
Completed

**What we'll implement:**
- Initialize a Spring Boot project with Gradle
- Configure MongoDB connection
- Set up basic project structure following package-by-feature organization
- Create the User data model
- Implement health check using Spring Boot Actuator

**Development tasks:**
1. Create a new Spring Boot project with Gradle
2. Add Spring Boot Actuator for health checks
3. Configure MongoDB connection in application.yml
4. Set up package structure
5. Implement the User model with validation using Lombok
6. Write tests for the User model

**Completed Implementation:**
- Set up a Spring Boot project with Gradle using Java 21
- Added Lombok to reduce boilerplate code
- Implemented Spring Boot Actuator for health checks at `/api/v1/health`
- Created MongoDB configuration
- Implemented the User model with proper validation
- Added comprehensive test cases for the User model
- Configured logback for structured JSON logging

## Iteration 2: User Repository Layer
**Status**: Completed

**What we'll implement:**
- Create the MongoDB repository for User entities
- Implement custom repository methods for case-insensitive email handling
- Write unit tests for the repository layer

**Development tasks:**
1. Create UserRepository interface extending MongoRepository
2. Implement custom methods for email uniqueness validation
3. Write tests for basic CRUD operations and custom methods
4. Configure test environment with mocked MongoDB

**Completed Implementation:**
- Created UserRepository interface extending MongoRepository<User, UUID>
- Added methods for case-insensitive email matching:
  - findByEmailIgnoreCase() to find users by email regardless of case
  - existsByEmailIgnoreCase() to check if an email already exists
- Created custom repository interface and implementation for specialized operations:
  - isEmailUnique() to verify email uniqueness with an option to exclude a specific user
  - findByExactEmail() for exact case-sensitive email matching
- Wrote comprehensive test cases:
  - Unit tests for the custom repository implementation
  - Integration tests for the repository interface methods
  - Used Mockito to mock MongoDB interactions

**Actual cost:** $0.30

## Iteration 3: User Service Layer
**Status**: Completed

**What we'll implement:**
- Create the service layer for handling business logic
- Implement CRUD operations in the service
- Add validation logic for user input
- Write unit tests for the service layer

**Development tasks:**
1. Create UserService interface
2. Implement UserServiceImpl with required CRUD operations
3. Add validation logic for user data
4. Write comprehensive tests for the service layer using Mockito

**Completed Implementation:**
- Created a set of common exceptions for error handling:
  - ResourceNotFoundException for when requested resources don't exist
  - DuplicateResourceException for email uniqueness violations
  - BadRequestException for invalid input data
- Created UserService interface with well-defined operations:
  - getUserById(), getAllUsers(), createUser()
  - updateUser(), patchUser(), deleteUser()
  - existsByEmail(), getUserByEmail()
- Implemented UserServiceImpl with:
  - Proper validation of inputs
  - Email uniqueness checks
  - Structured error handling
  - Logging for all operations
- Wrote comprehensive tests covering:
  - All successful operations
  - Edge cases and error conditions
  - Validation logic
  - Email uniqueness constraints

**Actual cost:** $0.40

## Iteration 4: User Controller - Read Operations
**Status**: Completed

**What we'll implement:**
- Create the REST controller for User endpoints
- Implement GET endpoint for retrieving users by ID
- Implement GET endpoint for retrieving all users or search by email
- Set up error handling for common scenarios
- Write unit and integration tests

**Development tasks:**
1. Create UserController with proper request mapping
2. Implement GET by ID endpoint
3. Implement GET all users endpoint with optional email search
4. Add error handling for user not found scenarios
5. Write tests for the controller endpoints

**Completed Implementation:**
- Created UserController with base path `/api/v1/users`
- Implemented REST-compliant endpoints:
  - `GET /api/v1/users/{id}` to retrieve a user by ID
  - `GET /api/v1/users` to retrieve all users
  - `GET /api/v1/users?email=...` to search for a user by email
- Added proper exception handling for 404 Not Found scenarios
- Documented API with OpenAPI annotations for Swagger
- Implemented comprehensive tests:
  - Unit tests for all endpoints using MockMvc
  - Integration tests to verify endpoint behavior
  - Test cases for both success and error scenarios

**Actual cost:** $0.35

## Iteration 5: User Controller - Write & Update Operations
**Status**:  
Completed

**What we'll implement:**
- POST endpoint for creating users
- PUT endpoint for full updates
- PATCH endpoint for partial updates 
- Common validation for all write operations

**Development tasks:**
1. Implement POST, PUT, and PATCH endpoints in UserController
2. Add consistent request validation using Bean Validation
3. Handle duplicate email errors and validation failures
4. Write focused tests for all operations

**Sample prompt:**
"Implement POST endpoint in UserController.java to create new users. Include validation for email format and uniqueness. Add unit tests in UserControllerTest.java."

**Completed Implementation:**
- Added POST endpoint for creating users with proper validation
- Implemented PUT endpoint for full user updates
- Added PATCH endpoint for partial user updates
- Created customized ObjectMapper for proper ObjectId serialization
- Added comprehensive test cases for all endpoints
- Implemented proper HTTP status codes and error responses
- Properly documented endpoints with OpenAPI annotations

**Actual cost:** $0.30

## Iteration 6: User Controller - Delete Operation
**Status**:  
Completed

**What we'll implement:**
- DELETE endpoint for removing users
- Proper HTTP status code handling

**Development tasks:**
1. Implement DELETE endpoint with proper error handling
2. Write focused tests for delete operation

**Sample prompt:**
"Add DELETE endpoint to UserController.java for removing users by ID. Return 204 on success, 404 if user not found."

**Completed Implementation:**
- Implemented DELETE endpoint with proper HTTP status codes
- Added comprehensive test cases for the endpoint
- Properly documented the endpoint with OpenAPI annotations
- Ensured proper validation and error handling

**Actual cost:** $0.20

## Iteration 7: API Documentation, Logging & Final Polish
**Status**:  
Completed

**What we'll implement:**
- OpenAPI documentation with Springdoc
- JSON structured logging
- Final integration tests and cleanup

**Development tasks:**
1. Configure Springdoc OpenAPI and add annotations
2. Set up JSON logging and request/response tracking
3. Add comprehensive integration tests
4. Verify specification requirements

**Completed Implementation:**
- Configured OpenAPI documentation with Springdoc and interactive Swagger UI
- Added detailed OpenAPI annotations to all controller endpoints
- Implemented request/response logging for debugging with a custom filter
- Set up structured JSON logging with Logback
- Verified our implementation against all specification requirements
- Updated the README with complete documentation of the API
- Ensured the codebase follows the package-by-feature structure

**Actual cost:** $0.25

## Common Commands Reference
```
# Run tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.example.userservice.user.controller.UserControllerTest"

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

## Sample CLAUDE.md Content
```markdown
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

## Coding Conventions
- Use Lombok annotations to reduce boilerplate
- Controller methods return ResponseEntity<?>
- Service methods throw specific exceptions rather than returning null
- Repository methods follow Spring Data naming conventions
- Tests follow given/when/then pattern

## Key Files
- User model: `src/main/java/com/example/userservice/user/model/User.java`
- Main controller: `src/main/java/com/example/userservice/user/controller/UserController.java`
- Service interface: `src/main/java/com/example/userservice/user/service/UserService.java`
- Main config: `src/main/resources/application.yml`
```