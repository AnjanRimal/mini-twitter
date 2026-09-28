# 🐦 Mini Twitter API

A Twitter-style social REST API built with **Spring Boot 4**, **Spring Security** and **JWT authentication**. Users register, log in, post tweets, follow each other, like tweets and read a personalized timeline.

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)

## ✨ Features

- **Stateless JWT authentication**: register and log in to receive a token, then send it as `Authorization: Bearer <token>`. A custom `JwtFilter` validates it on every request.
- **Password hashing** with Spring Security, and role-based users (`ROLE_USER`)
- **Tweets**: post, delete your own, and like
- **Social graph**: follow and unfollow users (self-referencing many-to-many)
- **Timeline**: tweets from everyone you follow plus your own, newest first
- **Validation** on requests (`@Valid`, `@NotBlank`, `@Email`) and a **global exception handler** for consistent error responses
- **DTOs**, so API responses never expose password hashes or JPA entities

## 🏗️ Architecture

```
src/main/java/com/anjan/mini_twitter
├── controller/   AuthController, TweetController, UserController
├── service/      AuthService, TweetService, UserService, UserDetailsServiceImpl
├── security/     SecurityConfig, JwtFilter, JwtUtil
├── repository/   Spring Data JPA repositories
├── entity/       User, Tweet, Role
├── dto/          Request and response objects
└── exception/    GlobalExceptionHandler
```

```
User ──< Tweet            (author)
User >──< User            (following / followers)
User >──< Tweet           (likes)
```

## 🔌 API endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/auth/register` | – | Create an account, returns a JWT |
| `POST` | `/auth/login` | – | Log in, returns a JWT |
| `POST` | `/tweets` | ✅ | Post a tweet |
| `GET` | `/tweets/timeline` | ✅ | Your timeline |
| `GET` | `/tweets/user/{userId}` | – | A user's tweets |
| `POST` | `/tweets/{id}/like` | ✅ | Like a tweet |
| `DELETE` | `/tweets/{id}` | ✅ | Delete your tweet |
| `GET` | `/users/{username}` | – | View a profile |
| `POST` | `/users/{id}/follow` | ✅ | Follow a user |
| `POST` | `/users/{id}/unfollow` | ✅ | Unfollow a user |

## 🚀 Getting started

**Prerequisites:** Java 21 and MySQL 8

```sql
CREATE DATABASE twitterdb;
```

Copy the config template and fill in your own values. `application.properties` is git-ignored, so your credentials stay local.

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Set `spring.datasource.password`, and set `app.jwt.secret` to a long random string. You can generate one with `openssl rand -base64 48`.

```bash
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

## 🧪 Try it

Ready-made requests are in [`test.http`](test.http). Or use curl:

```bash
# Register and capture the token
TOKEN=$(curl -s -X POST localhost:8080/auth/register -H "Content-Type: application/json" \
  -d '{"username":"anjan","email":"anjan@example.com","password":"secret123"}' | jq -r .token)

# Tweet, then read your timeline
curl -X POST localhost:8080/tweets -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" -d '{"content":"Hello from Mini Twitter!"}'
curl localhost:8080/tweets/timeline -H "Authorization: Bearer $TOKEN"
```

## 🛣️ Roadmap
- [ ] Pagination on the timeline
- [ ] Redis caching for hot timelines (config is already in place)
- [ ] Replies and retweets
- [ ] Integration tests with Testcontainers
- [ ] Docker Compose (app + MySQL + Redis)

## 👤 Author
**Anjan Rimal** · [Portfolio](https://anjanrimal.com/) · [LinkedIn](https://www.linkedin.com/in/anjanrimal) · [GitHub](https://github.com/AnjanRimal)
