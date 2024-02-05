
## Project Overview: "Person Security"
This project is a Spring Boot application using Spring Security and Spring Data JPA for user and role management. Additionally, it utilizes Flyway for database migration and TestContainers for integration testing.

Building the Project
To build the project, use the following Maven command:

mvn clean install
Initializing the Database
The project uses PostgreSQL as its database. You can run a Docker container with PostgreSQL using the following docker-compose file:


## Key Endpoints
### User Registration:

Endpoint: POST /api/v1/registration
Description: Register a new user with a unique username.
Request Body (JSON):

Copy code
{
  "username": "example",
  "password": "password123",
  "role": "USER"
}

### Get Greeting:

Endpoint: GET /api/v1/greetings
Description: Get a welcome message for an authenticated user.
Requirements: Authorization via HTTP Basic with valid credentials.


### Admin Page:

Endpoint: GET /api/v1/admin
Description: Get a welcome message for a user with the "ADMIN" role.
Requirements: Authorization via HTTP Basic with valid admin credentials.

### Assign "ADMIN" Role to a User:

Endpoint: GET /api/v1/make-admin/{username}
Description: Change a user's role to "ADMIN". Admin authorization is required.
Running the Application

### Initializing
Start the PostgreSQL container using the provided docker-compose file.
Build and run the Spring Boot application with the command mvn spring-boot:run.
The application will be accessible at http://localhost:8080.
Initialization for Testing
For integration testing, the project includes the AbstractIntegrationServiceTest class, which uses TestContainers to set up a PostgreSQL container. Ensure you have Docker installed, and your environment supports TestContainers.

### Notes
Make sure your development environment supports Lombok for automatic generation of getters, setters, and other methods.

You might need to configure your IDE to work correctly with a project using Lombok.

If necessary, update Maven dependency versions and adjust the Java version according to your requirements.
