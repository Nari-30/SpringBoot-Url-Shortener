# Shrinkr Backend

The backend for Shrinkr, a secure and modern URL shortener platform with JWT authentication, custom aliases, QR code generation, expiry countdown timers, and private user dashboards. Built with Java 21 and Spring Boot, backed by MySQL and Spring Security, featuring real-time analytics and a responsive frontend UI.

**Live:** [Url Shortener](https://springboot-url-shortener-production.up.railway.app)


**Stack:** Java 21 · Spring Boot · Spring Security · JWT · MySQL · Hibernate/JPA · HTML · CSS · JavaScript · QRCode.js

## Features

- **JWT Authentication:** Secure user registration and login using Spring Security and JWT tokens

- **Private User Dashboards:** Each user can manage and view only their own shortened URLs

- **Custom Aliases:** Create personalized short links with unique custom alias names

- **Real-Time Click Analytics:** Live click tracking with auto-refresh dashboard updates

- **Expiry Countdown Timer:** URLs can expire after a custom duration with live countdown display

- **QR Code Generation:** Generate QR codes instantly for every shortened URL

- **URL Deletion:** Users can securely delete their own shortened links

- **Responsive Modern UI:** Fully responsive frontend built using HTML, CSS, and JavaScript

- **MySQL Database Integration:** Persistent URL and user data storage using MySQL and Hibernate/JPA

- **Secure Redirect Handling:** Fast and secure URL redirection with Spring Boot backend APIs

- **Copy-to-Clipboard Support:** One-click copy functionality for shortened URLs

- **Protected APIs:** Secure backend endpoints with Spring Security authorization

- **Custom Expiry Support:** Optional URL expiration in hours for temporary links

- **Live Dashboard Statistics:** Displays total links, total clicks, and recent URLs dynamically

## Architecture

```text
Browser / Frontend
(HTML, CSS, JavaScript Dashboard UI)
            |
            v

Spring Boot Backend
(Java 21 + Spring Security + JWT)

            |
            |-- Authentication Layer
            |      |-- User Registration
            |      |-- User Login
            |      +-- JWT Authorization
            |
            |-- URL Management
            |      |-- URL Shortening
            |      |-- Custom Aliases
            |      |-- Expiry Timer
            |      |-- QR Code Generation
            |      +-- Redirect Handling
            |
            |-- Analytics System
            |      |-- Click Tracking
            |      |-- Live Dashboard Stats
            |      +-- Recent URLs
            |
            +-- MySQL Database
                   |-- users table
                   |-- url_mapping table
                   +-- Persistent Storage
```

## Project Structure

```text
src/main/java/UrlShortener/

├── UrlShortenerApplication.java

├── config/
│   ├── SecurityConfig.java
│   ├── JWTFilter.java
│   └── JWTUtil.java

├── controller/
│   ├── AuthController.java
│   └── UrlController.java

├── model/
│   ├── User.java
│   ├── UrlMapping.java
│   ├── UrlRequest.java
│   ├── RegisterRequest.java
│   └── LoginRequest.java

├── repository/
│   ├── UserRepository.java
│   └── UrlRepository.java

├── service/
│   ├── AuthService.java
│   └── UrlService.java

└── resources/
    ├── static/
    │   ├── index.html
    │   ├── login.html
    │   ├── register.html
    │   ├── dashboard.html
    │   ├── style.css
    │   └── script.js
    │
    └── application.properties
```

## API

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | none | Register a new user |
| POST | `/api/auth/login` | none | Login and receive JWT token |
| POST | `/api/shorten` | JWT Token | Create a shortened URL |
| GET | `/r/{shortCode}` | none | Redirect to original URL |
| GET | `/api/analytics` | JWT Token | Fetch user dashboard analytics |
| DELETE | `/api/delete/{shortCode}` | JWT Token | Delete a shortened URL |

## Request Body Examples

### Register User

```json
{
  "username": "narendhra",
  "email": "narendhra@gmail.com",
  "password": "password123"
}
```

---

### Login User

```json
{
  "username": "narendhra",
  "password": "password123"
}
```

---

### Create Short URL

```json
{
  "originalUrl": "https://github.com",
  "customAlias": "github",
  "expiryHours": 2
}
```

---

### Create URL Without Alias

```json
{
  "originalUrl": "https://openai.com"
}
```

---

### Authorization Header

```text
Authorization: Bearer <jwt_token>
```

## Security & System Design

### JWT Authentication
Secure API authentication using JSON Web Tokens (JWT) with Spring Security. Protected endpoints require a valid JWT token in the Authorization header.

### Private User Dashboards
Each user can access and manage only their own shortened URLs. URL ownership is linked using database relationships between users and URL mappings.

### Custom Alias Validation
Custom aliases are globally unique to prevent redirect conflicts and duplicate short URLs.

### Expiry System
URLs can optionally expire after a custom number of hours. Expired links automatically stop redirecting and display as expired in the dashboard.

### Secure Password Handling
User passwords are securely stored using encrypted password hashing with Spring Security.

### Real-Time Analytics
Dashboard analytics update automatically using periodic frontend refresh intervals for live click tracking.

### QR Code Integration
QR codes are dynamically generated for shortened URLs using QRCode.js for easy sharing and mobile access.

### Database Persistence
All users, URLs, analytics, clicks, aliases, and expiry data are persistently stored using MySQL and Hibernate/JPA.

## Running Locally

Make sure you have the following installed:

- Java 21
- MySQL
- Maven

Clone the repository:

```bash
git clone https://github.com/your-username/shrinkr-backend.git

cd shrinkr-backend
```

---

### Create MySQL Database

```sql
CREATE DATABASE urlshortener;
```

---

### Configure Database

Update your `application.properties` file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/urlshortener

spring.datasource.username=root

spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

---

### Run the Application

```bash
mvn spring-boot:run
```

---

### Access Application

```text
Frontend:
http://localhost:8080

Backend API:
http://localhost:8080/api
```

---

### Test API

```bash
curl -X POST http://localhost:8080/api/shorten \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <jwt_token>" \
-d '{
  "originalUrl":"https://github.com",
  "customAlias":"github",
  "expiryHours":2
}'
```

## Environment Variables

| Variable | Used For |
|---|---|
| `SPRING_DATASOURCE_URL` | MySQL database JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | MySQL database username |
| `SPRING_DATASOURCE_PASSWORD` | MySQL database password |
| `JWT_SECRET` | Secret key for JWT token generation |
| `SERVER_PORT` | Spring Boot server port |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Hibernate database update strategy |
| `RAILWAY_PUBLIC_DOMAIN` | Public Railway deployment domain |

---

## Local Storage Usage

JWT authentication tokens are securely stored in browser localStorage after login.

If localStorage is cleared:

- User will be logged out
- Dashboard access will require login again
- Previously shortened URLs will still work normally
- URL data remains safely stored in the MySQL database

---

## Deployment

The application is deployed using Railway.

**Live:** [Url Shortener](https://springboot-url-shortener-production.up.railway.app)

## Author

**Sadi Narendhra**

Java Full Stack Developer | Spring Boot & Web Development Enthusiast
