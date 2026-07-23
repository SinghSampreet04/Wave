# 🌊 Wave

> A production-ready, real-time team collaboration platform inspired by Slack,
> Microsoft Teams, and Discord, featuring workspaces, channels, direct messages,
> threads, file sharing, notifications, presence, search, and live synchronization.

---

## 📖 Overview

Wave is a full-stack communication platform designed for organizations and
teams. Users can create workspaces, organize conversations into public or
private channels, exchange direct messages, share files, collaborate in threads,
and receive live updates without refreshing the page.

The project uses a feature-based architecture across both the React frontend
and Spring Boot backend. PostgreSQL stores durable application data, Redis
supports coordination and rate limiting, and STOMP WebSockets synchronize
connected clients in real time.

---

## ✨ Features

### 👤 Authentication and Accounts

- User Registration and Login
- JWT Access Token Authentication
- Rotating Refresh Sessions
- HttpOnly Refresh Cookies
- BCrypt Password Hashing
- Email Verification
- Forgot and Reset Password
- Protected Routes and API Endpoints
- Editable Profile and Presence Preference
- Secure Logout and Session Revocation

---

### 🏢 Workspaces and Channels

- Create, Edit, and Delete Workspaces
- Workspace Roles and Membership
- Invite Registered Users by Email
- Accept or Reject Workspace Invitations
- Create and Delete Public or Private Channels
- Private Channel Membership
- Channel Invitations
- Role-Based Workspace and Channel Authorization

---

### 💬 Real-Time Messaging

- Channel Messaging
- Direct Messages
- Message Editing
- Soft Message Deletion
- Thread Replies
- Emoji Reactions
- Mentions and Mention Highlighting
- Typing Indicators
- Message Pinning
- Read States
- File Attachments
- Authenticated File Downloads
- Retryable Message Sending
- Offline Draft Preservation
- Cursor-Based Message History

---

### 🔔 Collaboration

- Live Notification Center
- Unread Notification Counts
- Mark One or All Notifications as Read
- Invitation Notifications
- Mention Notifications
- Thread Reply Notifications
- Direct Message Notifications
- Reaction Notifications
- Online, Away, Busy, Invisible, and Offline Presence
- Workspace and Channel Search
- Direct Conversation Search
- Real-Time Reconnect and State Synchronization

---

### 🛡 Platform and Operations

- Feature-Based Backend and Frontend Architecture
- Spring Security Authorization
- PostgreSQL Persistence
- Flyway Database Migrations
- Redis Caching and Rate Limiting
- Spring Actuator Health Probes
- Prometheus Metrics
- Dockerized Services
- Persistent Database and Upload Volumes
- Automated HTTPS with Caddy
- WebSocket Reverse Proxying
- Container Health Checks and Resource Limits
- Backup, Restore, and Smoke-Test Scripts
- GitHub Actions CI Pipeline

---

## 🛠 Tech Stack

### Frontend

- React 19
- TypeScript
- React Router
- TanStack React Query
- Zustand
- React Hook Form
- Zod
- Axios
- STOMP.js
- Vite

### Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring WebSocket with STOMP
- JWT Authentication
- Hibernate
- Flyway
- Maven

### Data

- PostgreSQL 17
- Redis 7
- Persistent Local File Storage

### Testing

- JUnit
- Mockito
- Spring Security Test
- Testcontainers
- Vitest
- React Testing Library
- Playwright

### Infrastructure

- Docker
- Docker Compose
- Caddy
- Nginx
- GitHub Actions

---

# 🏗 Architecture

```text
                         Browser Clients
                               │
                         HTTPS / WSS
                               │
                               ▼
                         Caddy Edge Proxy
                               │
              ┌────────────────┴────────────────┐
              │                                 │
              ▼                                 ▼
     React / TypeScript UI             Spring Boot Backend
       served by Nginx                   REST + STOMP
                                                │
                         ┌──────────────────────┼──────────────────────┐
                         │                      │                      │
                         ▼                      ▼                      ▼
                  PostgreSQL 17             Redis 7            File Storage
                  Durable Data       Cache / Rate Limiting      Attachments
```

REST endpoints provide authoritative application state. Normalized WebSocket
events update React Query caches across connected clients for messages,
reactions, threads, notifications, typing, read states, and presence.

---

# 📂 Project Structure

```text
Wave
│
├── backend
│   ├── src/main/java/com/wave/backend
│   │   ├── auth
│   │   ├── workspace
│   │   ├── workspaceinvite
│   │   ├── channel
│   │   ├── channelinvite
│   │   ├── message
│   │   ├── directmessage
│   │   ├── reaction
│   │   ├── notification
│   │   ├── websocket
│   │   └── config
│   ├── src/main/resources/db/migration
│   ├── src/test
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend
│   ├── src
│   │   ├── features
│   │   │   ├── auth
│   │   │   ├── workspace
│   │   │   ├── channel
│   │   │   ├── chat
│   │   │   ├── direct
│   │   │   ├── notification
│   │   │   ├── search
│   │   │   └── websocket
│   │   ├── components
│   │   ├── layouts
│   │   └── pages
│   ├── e2e
│   ├── Dockerfile
│   └── package.json
│
├── ops
│   ├── backup.sh
│   ├── restore.sh
│   └── smoke-test.sh
│
├── compose.yaml
├── Caddyfile
└── README.md
```

---

# ⚙️ Local Installation

## Prerequisites

- Java 17
- Node.js 20 or newer
- Docker Desktop
- Git

## Clone Repository

```bash
git clone https://github.com/SinghSampreet04/Wave.git
cd Wave
```

---

## Backend

```bash
cd backend
cp .env.example .env
docker compose --env-file .env up --build
```

Backend API:

```text
http://localhost:8082
```

Health endpoint:

```text
http://localhost:8082/actuator/health
```

---

## Frontend

Open another terminal:

```bash
cd frontend
cp .env.example .env
npm ci
npm run dev
```

Frontend:

```text
http://localhost:5173
```

---

# 🔐 Security

- Short-Lived JWT Access Tokens
- Rotating and Hashed Refresh Sessions
- HttpOnly and SameSite Refresh Cookies
- BCrypt Password Hashing
- Role-Based Authorization
- Workspace and Channel Access Validation
- Authenticated WebSocket Connections
- Private Content Search Filtering
- Login and Account Recovery Rate Limiting
- File Type and Size Validation
- Exact-Origin CORS Configuration
- Non-Root Production Containers
- Internal PostgreSQL and Redis Networking
- Security Headers and Automatic HTTPS

---

# 🧪 Testing

Backend:

```bash
cd backend
./mvnw verify
```

Frontend:

```bash
cd frontend
npm run lint
npm test -- --run
npm run build
```

End-to-end:

```bash
cd frontend
npx playwright test
```

---

# 🚀 Production Deployment

Wave includes a single-VPS Docker Compose stack with Caddy, Nginx, Spring Boot,
PostgreSQL, Redis, persistent uploads, automated TLS, health checks, log
rotation, and resource limits.

```bash
cp .env.example .env
docker compose config
docker compose up --build -d
docker compose ps
```

Production environment values are defined in `.env.example`. A domain,
PostgreSQL password, JWT signing secret, ACME email, and SMTP credentials are
required before public deployment.

Operational commands:

```bash
./ops/backup.sh
./ops/restore.sh ./backups/<timestamp>
PUBLIC_ORIGIN=https://wave.example.com ./ops/smoke-test.sh
```

---

# 👨‍💻 Author

**Sampreet Singh**

GitHub: [github.com/SinghSampreet04](https://github.com/SinghSampreet04)

---

## ⭐ If you like this project, consider giving it a star!
