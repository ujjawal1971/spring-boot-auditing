# Spring Boot JPA Auditing with Hibernate Envers

A comprehensive Spring Boot application demonstrating entity auditing using JPA annotations and Hibernate Envers.

## Features

### JPA Auditing
- `@CreatedDate` - Auto-fills creation timestamp
- `@LastModifiedDate` - Auto-fills last modification timestamp
- `@CreatedBy` - Auto-fills creator username
- `@LastModifiedBy` - Auto-fills last modifier username
- `@EnableJpaAuditing` - Global auditing configuration

### Hibernate Envers
- `@Audited` - Enable audit logging for entities
- Complete revision history tracking
- Query historical versions of data
- Audit trail reports
- Track WHAT changed, WHO changed it, WHEN

## Tech Stack
- Java 21
- Spring Boot 4.0.4
- Spring Data JPA
- Hibernate 7.2.7
- Hibernate Envers 7.2.7
- MySQL 8.0
- ModelMapper 3.2.0
- Lombok 1.18.44

## Project Structure
```
src/main/java/com/example/spring_boot_project_demo/prod_ready_features/
├── auth/
│   ├── AuditorAwareImpl.java      // Implements AuditorAware interface
│   └── AuditorAware.java          // JPA auditor provider
├── config/
│   └── AppConfig.java             // @EnableJpaAuditing configuration
├── controllers/
│   ├── PostController.java        // REST endpoints for posts
│   └── AuditController.java       // Audit trail endpoints
├── entities/
│   ├── PostEntity.java            // JPA entity with @Audited
│   └── AuditableEntity.java       // Base entity with audit fields
├── services/
│   ├── PostService.java           // Service interface
│   └── PostServiceImp.java        // Service implementation
├── dto/
│   └── PostDTO.java               // Data transfer object
├── repositories/
│   └── PostRepository.java        // Spring Data JPA repository
└── exceptions/
    └── ResourceNotFoundException.java
```

## Setup Instructions

### Prerequisites
- Java 21+
- MySQL 8.0+
- Maven 3.9+
- IntelliJ IDEA (recommended)

### Installation

1. **Clone the repository**
```bash
   git clone https://github.com/YOUR-USERNAME/spring-boot-auditing.git
   cd spring-boot-auditing
```

2. **Configure Database**
   
   Edit `src/main/resources/application.properties`:
```properties
   spring.application.name=prod-ready-features
   server.port=8080
   
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   
   spring.datasource.url=jdbc:mysql://localhost:3306/prod-ready-features?useSSL=false
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

3. **Build the Project**
```bash
   mvn clean install
```

4. **Run the Application**
```bash
   mvn spring-boot:run
```

   Application starts at: `http://localhost:8080`

## API Endpoints

### Posts Management
- **Create Post**
```bash
  POST /posts
  Content-Type: application/json
  
  {
    "title": "My Blog Post",
    "description": "Post content",
    "createdBy": "user1"
  }
```

- **Get All Posts**
```bash
  GET /posts
```

- **Get Post by ID**
```bash
  GET /posts/{id}
```

- **Update Post**
```bash
  PUT /posts/{id}
  Content-Type: application/json
  
  {
    "title": "Updated Title",
    "description": "Updated content",
    "createdBy": "user1"
  }
```

- **Delete Post**
```bash
  DELETE /posts/{id}
```

### Audit Trail
- **Get All Revisions of a Post**
```bash
  GET /audit/posts/{postId}
```
  
  Returns all historical versions of the post with revision numbers.

## Database Schema

### posts table
```sql
CREATE TABLE posts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_by VARCHAR(255),
    created_date DATETIME,
    modified_by VARCHAR(255),
    modified_date DATETIME
);
```

### posts_aud table (Auto-created by Envers)
```sql
CREATE TABLE posts_aud (
    id BIGINT,
    title VARCHAR(255),
    description TEXT,
    created_by VARCHAR(255),
    created_date DATETIME,
    modified_by VARCHAR(255),
    modified_date DATETIME,
    rev INT,
    revtype TINYINT
);
```

### revinfo table (Auto-created by Envers)
```sql
CREATE TABLE revinfo (
    rev INT PRIMARY KEY AUTO_INCREMENT,
    revtstmp BIGINT
);
```

## Key Concepts

### JPA Auditing vs Hibernate Envers

| Feature | JPA Auditing | Hibernate Envers |
|---------|-------------|------------------|
| Auto-fills timestamps | ✅ YES | ❌ NO |
| Auto-fills user info | ✅ YES | ❌ NO |
| Tracks full history | ❌ NO | ✅ YES |
| Query old versions | ❌ NO | ✅ YES |
| Separate audit tables | ❌ NO | ✅ YES |

### RevType Values (Envers)
- **0** = ADD (New record inserted)
- **1** = MOD (Record modified)
- **2** = DEL (Record deleted)

## Configuration

### Enable JPA Auditing
```java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "getAuditorAwareImpl")
public class AppConfig {
    
    @Bean
    AuditorAware<String> getAuditorAwareImpl() {
        return new AuditorAwareImpl();
    }
    
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
```

### Implement AuditorAware
```java
@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("system");
        // In production: get from SecurityContext
    }
}
```

### Enable Auditing on Entity
```java
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
public class PostEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    
    @CreatedDate
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    
    @CreatedBy
    private String createdBy;
    
    @LastModifiedBy
    private String modifiedBy;
}
```

## Example Usage

### Create a Post
```bash
curl -X POST http://localhost:8080/posts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot Auditing",
    "description": "Learn JPA auditing",
    "createdBy": "user1"
  }'
```

### Update the Post
```bash
curl -X PUT http://localhost:8080/posts/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot Auditing Guide",
    "description": "Complete JPA auditing tutorial",
    "createdBy": "user1"
  }'
```

### View Audit History
```bash
curl http://localhost:8080/audit/posts/1
```

Response shows all revisions with timestamps and changes.

## Learning Points

This project demonstrates:
- ✅ Spring Data JPA repository pattern
- ✅ Entity lifecycle callbacks (@CreatedDate, @LastModifiedDate)
- ✅ Auditor awareness implementation
- ✅ Hibernate Envers integration
- ✅ REST API best practices
- ✅ DTO pattern with ModelMapper
- ✅ Exception handling
- ✅ Service-Controller-Repository architecture

## Next Steps

- [ ] Add Spring Security for real user tracking
- [ ] Implement pagination for large datasets
- [ ] Add Swagger/SpringFox for API documentation
- [ ] Add unit tests with JUnit 5
- [ ] Add integration tests
- [ ] Implement soft delete with Envers
- [ ] Deploy to AWS/Heroku
- [ ] Add Docker support

## Common Issues & Solutions

### Issue: posts_aud table not created
**Solution:** Ensure `@Audited` is on entity and Hibernate Envers dependency is present.

### Issue: Auditor not found
**Solution:** Ensure `@EnableJpaAuditing` references correct bean name in `auditorAwareRef`.

### Issue: createdDate/createdBy are null
**Solution:** Ensure `@EntityListeners(AuditingEntityListener.class)` is on entity.

## Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-envers</artifactId>
    <version>7.2.7.Final</version>
</dependency>

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.2.0</version>
</dependency>
```

## Author
Ujjawal Kumar

## License
MIT

## References
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate Envers Documentation](https://hibernate.org/orm/envers/)
- [JPA Auditing Guide](https://www.baeldung.com/database-auditing-jpa)
