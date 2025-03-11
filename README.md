# Java Spring REST API with MongoDB

This project implements a REST API using Spring Boot and MongoDB, following a controller-service-repository architecture pattern. It provides CRUD operations for managing users with proper validation and error handling.

## Project Overview

This is a Java 21 application built with:

- **Spring Boot**: Core framework
- **MongoDB**: Database
- **Gradle**: Build tool and dependency management
- **Lombok**: To reduce boilerplate code
- **JUnit 5 & Mockito**: For testing
- **Springdoc OpenAPI**: For API documentation
- **Logback**: For structured JSON logging

## Implementation Status

The implementation is complete with the following features:

1. **Project Setup and Basic Structure**
   - Spring Boot application with Gradle
   - MongoDB connection configuration
   - Package-by-feature organization
   - Spring Boot Actuator for health checks at `/api/v1/health`
   - Logback configured for structured JSON logging

2. **User Data Model**
   - User class with validation for fields (name, email)
   - Automatic trimming and case normalization for email
   - Unit tests for all validation rules

3. **Repository Layer**
   - MongoDB repository for User entities
   - Custom methods for case-insensitive email handling
   - Email uniqueness validation
   - Comprehensive test coverage

4. **Service Layer**
   - UserService interface defining all CRUD operations
   - Exception handling for common error scenarios
     - ResourceNotFoundException
     - DuplicateResourceException
     - BadRequestException
   - Implementation with business logic and validation
   - Comprehensive test coverage for all operations

5. **Controller Layer - Complete CRUD Operations**
   - REST endpoints for all user data operations
   - GET operations for retrieving users by ID and with filters
   - POST operation for creating new users
   - PUT operation for full updates of existing users
   - PATCH operation for partial updates of existing users
   - DELETE operation for removing users
   - OpenAPI/Swagger documentation
   - Unit and integration tests

6. **API Documentation & Logging**
   - OpenAPI 3.0 documentation with Springdoc
   - Interactive Swagger UI for API testing
   - JSON structured logging with Logback
   - Request/response logging for debugging
   - Comprehensive test coverage

## Prerequisites

- Java 21 JDK
- MongoDB (running locally on port 27017, or configured in application.yml)
- Gradle (or use the included Gradle wrapper)

## Running the Application

### Using Gradle wrapper

```bash
# Build the application
./gradlew build

# Run the application
./gradlew bootRun
```

### Using Java directly

```bash
# Build the application
./gradlew build

# Run the application
java -jar build/libs/user-service-0.0.1-SNAPSHOT.jar
```

## Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test classes
./gradlew test --tests "com.example.userservice.user.model.UserTest"
./gradlew test --tests "com.example.userservice.user.repository.*"
```

## API Endpoints

The API follows RESTful principles with URL versioning `/api/v1/...`.

### Currently Implemented:

- **Health Check**
  - `GET /api/v1/health` - Check if the service is running

- **Users Read Operations**
  - `GET /api/v1/users/{id}` - Retrieve a user by ID
  - `GET /api/v1/users` - Retrieve all users
  - `GET /api/v1/users?email=...` - Search for a user by email

### All Implemented Endpoints:

- **Users CRUD Operations**
  - `POST /api/v1/users` - Create a new user
  - `PUT /api/v1/users/{id}` - Full update of a user
  - `PATCH /api/v1/users/{id}` - Partial update of a user
  - `DELETE /api/v1/users/{id}` - Delete a user

## API Documentation

Once the application is running, you can access:
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI specification: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── userservice/
│   │               ├── UserServiceApplication.java
│   │               ├── common/
│   │               │   └── exception/
│   │               │       ├── BadRequestException.java
│   │               │       ├── DuplicateResourceException.java
│   │               │       └── ResourceNotFoundException.java
│   │               └── user/
│   │                   ├── controller/
│   │                   │   └── UserController.java
│   │                   ├── model/
│   │                   │   └── User.java
│   │                   ├── repository/
│   │                   │   ├── UserRepository.java
│   │                   │   ├── UserRepositoryCustom.java
│   │                   │   └── UserRepositoryCustomImpl.java
│   │                   └── service/
│   │                       ├── UserService.java
│   │                       └── UserServiceImpl.java
│   └── resources/
│       ├── application.yml
│       └── logback-spring.xml
└── test/
    └── java/
        └── com/
            └── example/
                └── userservice/
                    └── user/
                        ├── controller/
                        │   ├── UserControllerTest.java
                        │   └── UserControllerIntegrationTest.java
                        ├── model/
                        │   └── UserTest.java
                        ├── repository/
                        │   ├── UserRepositoryTest.java
                        │   └── UserRepositoryIntegrationTest.java
                        └── service/
                            └── UserServiceImplTest.java
```

## Features

The implementation includes all required features:

1. **Complete User CRUD Operations**: Full lifecycle management
2. **Swagger/OpenAPI Documentation**: Interactive API documentation
3. **Validation & Error Handling**: Comprehensive input validation and error responses
4. **Structured JSON Logging**: Request/response logging for debugging
5. **Health Check Endpoint**: For monitoring and deployment verification
6. **Test Coverage**: Unit and integration tests for all components

## Technical Details

### User Model

The `User` entity contains:
- `id` (UUID): Auto-generated by MongoDB
- `name` (String): Min length 4, max length 30
- `email` (String): Unique (case-insensitive), must be valid email format

Email addresses are:
- Trimmed before storage
- Converted to lowercase
- Validated for uniqueness (case-insensitive)

### Repository Implementation

The repository layer implements:
- Standard CRUD operations through MongoRepository
- Custom methods for case-insensitive email handling
- Email uniqueness validation with option to exclude specific user (for updates)