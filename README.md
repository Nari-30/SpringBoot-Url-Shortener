# Shrinkr Backend

The backend for Shrinkr, a secure and modern URL shortener platform with JWT authentication, custom aliases, QR code generation, expiry countdown timers, and private user dashboards. Built with Java 21 and Spring Boot, backed by MySQL and Spring Security, featuring real-time analytics and a responsive frontend UI.

**Live:** springboot-url-shortener-production.up.railway.app

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
