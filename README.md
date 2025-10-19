# Internet Cafe Management Server

## Project Structure

This is a Spring Boot backend server for Internet Cafe Management System using clean architecture principles.

### Directory Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/internetcafe/management/
│   │       ├── controller/          # REST Controllers
│   │       │   ├── auth/           # Authentication endpoints
│   │       │   ├── user/           # User management
│   │       │   ├── computer/       # Computer management
│   │       │   ├── session/        # Session management
│   │       │   └── payment/        # Payment processing
│   │       ├── service/            # Business Logic Layer
│   │       │   ├── impl/           # Service implementations
│   │       │   ├── user/           # User services
│   │       │   ├── computer/       # Computer services
│   │       │   ├── session/        # Session services
│   │       │   └── payment/        # Payment services
│   │       ├── repository/         # Data Access Layer
│   │       ├── entity/             # JPA Entities
│   │       ├── dto/                # Data Transfer Objects
│   │       ├── mapper/             # MapStruct mappers
│   │       ├── config/             # Configuration classes
│   │       ├── security/           # Security configuration
│   │       ├── exception/          # Custom exceptions
│   │       └── util/               # Utility classes
│   └── resources/
│       ├── static/                 # Static resources
│       ├── templates/              # Template files
│       ├── db/migration/           # Database migrations
│       ├── i18n/                   # Internationalization
│       └── application.yml         # Application configuration
├── test/
│   ├── java/                       # Test classes
│   └── resources/                  # Test resources
docs/                               # Documentation
├── api/                           # API documentation
└── database/                      # Database documentation
scripts/                           # Deployment scripts
logs/                              # Application logs
```

### Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Database**: MySQL (Production), H2 (Testing)
- **Security**: Spring Security + JWT
- **ORM**: Spring Data JPA + Hibernate
- **Migration**: Flyway
- **Mapping**: MapStruct
- **Build Tool**: Maven
- **Testing**: JUnit 5 + Mockito

### Key Features

- Clean Architecture implementation
- RESTful API design
- JWT-based authentication
- Database migration support
- Comprehensive testing structure
- Modular package organization
- Security configuration
- Internationalization support

### Getting Started

1. Ensure Java 17+ is installed
2. Install PostgreSQL (for production)
3. Configure database connection in `application.yml`
4. Run the application: `mvn spring-boot:run`

### Development Guidelines

- Follow clean architecture principles
- Separate concerns by package
- Use DTOs for API communication
- Implement proper exception handling
- Write comprehensive tests
- Follow REST API conventions
