# Wave - System Architecture

## Project Overview

Wave is a modern real-time collaboration platform inspired by Discord and Slack.

The system allows users to communicate in real time through workspaces, channels, messaging, file sharing, and notifications.

---

# Technology Stack

## Backend

- Java 17
- Spring Boot 3.5
- Spring Security
- Spring Data JPA
- PostgreSQL
- WebSocket (STOMP)
- JWT Authentication
- Maven

---

## Frontend

- React
- TypeScript
- Vite
- Tailwind CSS
- React Router
- Axios
- Zustand

---

## Infrastructure

- Docker
- Nginx
- GitHub Actions
- AWS EC2
- AWS S3
- Redis (future)

---

# High-Level Architecture

```
                React Frontend
                      │
                      │ REST API
                      ▼
            Spring Boot Backend
          ┌───────────┴───────────┐
          │                       │
     REST Controllers        WebSocket
          │                       │
          └───────────┬───────────┘
                      │
                  Services
                      │
             Business Logic
                      │
               Spring Data JPA
                      │
                 PostgreSQL
```

---

# Backend Modules

Authentication

- Register
- Login
- JWT
- Refresh Token

Users

- Profiles
- Settings
- Presence

Workspaces

- Create Workspace
- Invite Members
- Roles

Channels

- Public
- Private

Messages

- Text
- Reactions
- Threads
- Replies

Files

- Upload
- Download

Notifications

- Real-time
- Mentions

WebSocket

- Live Messaging
- Typing
- Presence

---

# Design Principles

- Modular Architecture
- Feature-based packages
- Stateless Authentication
- REST + WebSockets
- Secure by default
- Scalable
- Testable
- Production Ready