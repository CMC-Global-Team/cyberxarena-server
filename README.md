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
- **Containerization**: Docker + Docker Compose

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

#### Option 1: Using Docker (Recommended)

1. Ensure Docker and Docker Compose are installed
2. Clone the repository
3. Run the application with Docker Compose:
   ```bash
   docker-compose up -d
   ```
4. Access the application at `http://localhost:8080/api/v1`
5. Access phpMyAdmin at `http://localhost:8081` (optional)

#### Option 2: Local Development

1. Ensure Java 17+ is installed
2. Install MySQL (for production)
3. Configure database connection in `application.yml`
4. Run the application: `mvn spring-boot:run`

### Docker Commands

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Rebuild and start
docker-compose up --build -d

# Stop and remove volumes (WARNING: This will delete all data)
docker-compose down -v
```

### Services

- **Application**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/api/v1/swagger-ui.html
- **Health Check**: http://localhost:8080/api/v1/actuator/health
- **phpMyAdmin**: http://localhost:8081 (optional)
- **MySQL**: localhost:3307

### Development Guidelines

- Follow clean architecture principles
- Separate concerns by package
- Use DTOs for API communication
- Implement proper exception handling
- Write comprehensive tests
- Follow REST API conventions
