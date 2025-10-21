# Uber System

This is a Maven-based Spring Boot multi-module project for an Uber-like system. It contains two services:
- userService: Handles user registration, authentication, and authorization.
- driverService: Manages driver profiles and driver-specific APIs.

## Technologies Used
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- H2 Database
- Lombok
- JJWT (Java JWT)

## Getting Started
1. Build the project with Maven: `mvn clean install`
2. Run each service as a Spring Boot application.
3. Access H2 console at `/h2-console` for each service.

## Next Steps
- Implement entities, repositories, services, controllers, and security configuration as per requirements.
- See `.github/copilot-instructions.md` for Copilot customization.
