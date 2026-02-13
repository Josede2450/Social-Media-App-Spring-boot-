# Social Media Backend – Group Project

## 📌 Project Overview

This is the backend API for the Social Media Platform built with:

- Spring Boot
- MySQL
- JPA / Hibernate
- Google OAuth (to be configured later)

Each team member will run the project locally using their own MySQL instance.

---

# 🚀 Getting Started (For All Team Members)

Follow these steps before starting development.

---

## 1️⃣ Install Required Software

Make sure you have:

- Java 17 or 21
- IntelliJ IDEA
- MySQL Server
- MySQL Workbench

---

## 2️⃣ Create Local Database

Open MySQL Workbench and run:

```sql
CREATE DATABASE socialmedia;
Verify it exists:

SHOW DATABASES;
3️⃣ Configure application.properties
Inside:

src/main/resources/application.properties
Make sure you have:

spring.datasource.url=jdbc:mysql://localhost:3306/socialmedia
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
Replace YOUR_PASSWORD with your local MySQL password.

⚠ Do NOT push real passwords to GitHub in production.

4️⃣ Run the Application
Run:

SocialmediaApplication
Spring Boot will automatically create all tables.

After running, refresh MySQL Workbench and verify tables exist.



🚀 Social Media Backend – Development Plan
✅ Current Status

✔ Entities created

❌ Repositories not created

❌ Services not created

❌ Controllers not created

❌ Security not configured

Next phase: Build backend logic in structured layers.

🏗 Mandatory Architecture Rules

Every feature MUST follow this structure:

Controller → Service → Repository → Database


❌ Controllers must NOT access repositories directly
❌ No business logic inside controllers
✔ Services contain business logic
✔ Repositories handle database access

📁 Required Layer Order

Each member must implement in this order:

1️⃣ Repository
2️⃣ Service
3️⃣ Controller
4️⃣ DTOs

👤 Member 1 – Security (Google OAuth)
Responsibilities

Create SecurityConfig

Configure Google OAuth

Protect /api/** routes

Store user on first login

Tasks

Add Google OAuth config

Create authentication success handler

Extract google_id

Save user if not exists

Protect all endpoints except login

⚠ This member does NOT implement business logic for posts or chat.

👤 Member 2 – Chat System
Scope

Entities already exist:

Chat

ChatParticipant

ChatMessage

Step-by-Step Tasks

Create:

ChatRepository

ChatParticipantRepository

ChatMessageRepository

Create:

ChatService

ChatController

Implement endpoints:

POST   /api/chats
POST   /api/chats/{id}/participants
POST   /api/chats/{id}/messages
GET    /api/chats/{id}/messages

Logic Requirements

Only participants can send messages

Prevent duplicate participants

Validate chat exists

👥 Member 3 – Posts & Comments
Scope

Post

Comment

Step-by-Step Tasks

Create:

PostRepository

CommentRepository

Create:

PostService

PostController

Implement endpoints:

POST   /api/posts
GET    /api/posts
GET    /api/posts/{id}
DELETE /api/posts/{id}
POST   /api/posts/{id}/comments
DELETE /api/comments/{id}

Logic Requirements

Validate post exists

Only owner can delete post

Validate comment not empty

👤 Member 4 – Likes, Follow, Reports, Notifications
Scope

Like

Follow

Report

Notification

Step-by-Step Tasks

Create repositories:

LikeRepository

FollowRepository

ReportRepository

NotificationRepository

Create:

InteractionService

InteractionController

Implement endpoints:

POST   /api/posts/{id}/like
DELETE /api/posts/{id}/like
POST   /api/users/{id}/follow
DELETE /api/users/{id}/follow
POST   /api/posts/{id}/report
GET    /api/notifications

Logic Requirements

Prevent duplicate likes

Prevent duplicate follows

Create notification when:

Someone likes a post

Someone follows a user

📦 DTO Rules (Important)

Do NOT expose entities directly.

Create DTOs in:

dto/


Example:

PostRequest
PostResponse
CommentRequest
ChatMessageRequest

🔄 Git Workflow Rules

Each member must:

Create their own branch:

feature/security
feature/chat
feature/posts
feature/interactions


Pull before pushing

Test locally before merge

Never push broken code to main

🧪 Testing Standard

Before pushing:

Application must start

No console errors

Endpoint tested in Postman

📌 API Naming Standard

All controllers must start with:

@RequestMapping("/api")


Examples:

/api/posts
/api/chats
/api/users

🚨 Important

Everyone runs their own local database.

Data is NOT shared during development.

Only schema is shared.

Do not modify another member’s service logic without discussion.

📅 Development Order

Phase 1 → Repositories
Phase 2 → Services
Phase 3 → Controllers
Phase 4 → Security Integration
Phase 5 → Testing

🎯 Final Objective

By the end:

All CRUD endpoints working

Business logic separated properly

Security integrated

Clean layered architecture


Perfect 👍 here is a clean, short section only about handling sensitive data and connecting securely to the local database.

You can paste this into your README.

🔐 Handling Sensitive Data (Database Credentials)

To protect sensitive information, database credentials must NOT be stored directly inside application.properties.

The project uses environment variables instead.

📌 application.properties (Do NOT modify)
spring.datasource.url=jdbc:mysql://localhost:3306/socialmedia
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}


⚠ Never replace these placeholders with real credentials.

📌 How Each Team Member Connects to Their Local Database

Each developer must configure their own database credentials locally in IntelliJ.

Step 1 – Open Run Configuration
Run → Edit Configurations

Step 2 – Select SocialmediaApplication
Step 3 – Add Environment Variables

In the Environment Variables section, add:

DB_USERNAME=root
DB_PASSWORD=your_mysql_password


Example:

DB_USERNAME=root
DB_PASSWORD=MySecurePassword123!


Click Apply → OK
