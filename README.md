# ChatLLM - Spring Boot Chat Application

Μια Spring Boot εφαρμογή για chat με JWT authentication και integration με LLM models.

## 🚀 Features

- **JWT Authentication**: Secure login/register με JSON Web Tokens
- **Chat System**: Real-time chat functionality με bot responses
- **User Management**: User registration και profile management
- **Database Integration**: PostgreSQL/H2 database support με Hibernate
- **Security**: Spring Security με custom JWT filters
- **REST API**: RESTful endpoints για όλες τις λειτουργίες

## 🛠️ Technologies Used

- **Backend**: Spring Boot 3.x, Spring Security, Spring Data JPA
- **Database**: PostgreSQL (production), H2 (development)
- **Authentication**: JWT (JSON Web Tokens)
- **Build Tool**: Maven
- **Java Version**: 17+

## 📋 Prerequisites

- Java 17 ή νεότερο
- Maven 3.6+
- PostgreSQL (για production) ή H2 (για development)

## 🔧 Installation & Setup

### 1. Clone το repository
```bash
git clone https://github.com/YOUR_USERNAME/chatllm.git
cd chatllm
```

### 2. Configure Database
Επέλεξε μεταξύ PostgreSQL ή H2:

#### PostgreSQL (Production)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/llm_db
spring.datasource.username=llm_user
spring.datasource.password=llm_user
```

#### H2 (Development)
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
```

### 3. Configure JWT Secret
Στο `application.properties`:
```properties
app.jwt.secret=mySecretKeyThatIsAtLeast32CharactersLongForHMAC256AlgorithmSecurity
app.jwt.expiration-ms=3600000
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

Η εφαρμογή θα είναι διαθέσιμη στο: `http://localhost:8080`

## 📚 API Endpoints

### Authentication
```
POST /api/auth/register - User registration
POST /api/auth/login    - User login (returns JWT token)
```

### Chat (Requires Authentication)
```
POST /api/chat/send     - Send message to bot
```

### User Management (Requires Authentication)  
```
GET  /api/user/profile  - Get user profile
PUT  /api/user/profile  - Update user profile
```

## 🔐 Authentication Flow

1. **Register** νέος χρήστης:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "password123"}'
```

2. **Login** και πάρε JWT token:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "password123"}'
```

3. **Use token** για protected endpoints:
```bash
curl -X POST http://localhost:8080/api/chat/send \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello bot!"}'
```

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER'
);
```

### Chat Messages Table  
```sql
CREATE TABLE chat_messages (
    id BIGSERIAL PRIMARY KEY,
    sender VARCHAR(10) NOT NULL,
    message TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔧 Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar target/chatllm-*.jar
```

## 📝 TODO

- [ ] Real LLM integration
- [ ] WebSocket support για real-time chat
- [ ] User roles και permissions
- [ ] Chat history per user
- [ ] File upload support
- [ ] Docker containerization

## 🤝 Contributing

1. Fork το project
2. Δημιούργησε feature branch (`git checkout -b feature/amazing-feature`)
3. Commit τις αλλαγές σου (`git commit -m 'Add amazing feature'`)
4. Push στο branch (`git push origin feature/amazing-feature`)
5. Άνοιξε Pull Request



## 👨‍💻 Panos Foteinopoulos

**Your Name**
- GitHub: [@your-username](https://github.com/your-username)
- Email: your-email@example.com
