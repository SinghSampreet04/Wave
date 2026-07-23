# Wave

Wave is a full-stack team collaboration platform built around real-time
communication, access-controlled workspaces, and event-driven client updates.
The codebase uses feature-oriented modules on both the Spring Boot API and the
React client.

## Capabilities

- JWT authentication with rotating refresh sessions, email verification, and
  password recovery
- Workspace and channel membership, invitations, and role-based authorization
- Channel and direct messaging with editing, soft deletion, reactions, threads,
  typing indicators, mentions, pins, read states, and attachments
- Live notifications, presence, reconnect-safe WebSocket subscriptions, and
  access-controlled search
- PostgreSQL persistence, Redis-backed coordination and rate limiting, Flyway
  migrations, health probes, metrics, and persistent file storage

## Architecture

```text
Browser
  └─ Caddy
      ├─ React 19 / TypeScript / Vite
      └─ Spring Boot 3 / REST / STOMP
          ├─ PostgreSQL 17
          ├─ Redis 7
          └─ Persistent uploads
```

The backend is organized by domain feature; each feature owns its controllers,
services, DTOs, repositories, and entities. The frontend follows the same
boundary with feature-local APIs, hooks, state, components, and types. REST
provides authoritative state while normalized STOMP events update React Query
caches in real time.

## Technology

| Area | Stack |
| --- | --- |
| Backend | Java 17, Spring Boot, Spring Security, JPA, STOMP, Maven |
| Frontend | React 19, TypeScript, React Query, Zustand, Vite |
| Data | PostgreSQL, Redis, Flyway |
| Operations | Docker Compose, Caddy, Nginx, GitHub Actions |

## Repository

| Path | Purpose |
| --- | --- |
| `backend/` | Spring Boot API, domain modules, migrations, and tests |
| `frontend/` | React application and browser tests |
| `ops/` | Backup, restore, and deployment smoke checks |
| `compose.yaml` | Production service topology |
| `Caddyfile` | TLS termination and same-origin proxy rules |

## Development

Requirements: Java 17, Node.js 20+, and Docker.

```sh
cd backend
cp .env.example .env
docker compose --env-file .env up --build
```

```sh
cd frontend
cp .env.example .env
npm ci
npm run dev
```

Frontend: `http://localhost:5173`

API health: `http://localhost:8082/actuator/health`

## Verification

```sh
(cd backend && ./mvnw verify)
(cd frontend && npm run lint && npm test -- --run && npm run build)
```

## Production

The production Compose stack exposes only Caddy on ports 80 and 443. Application
services remain on an internal network and run with health checks, resource
limits, rotated logs, and persistent volumes.

```sh
cp .env.example .env
docker compose config
docker compose up --build -d
PUBLIC_ORIGIN=https://wave.example.com ./ops/smoke-test.sh
```

Operational commands:

```sh
./ops/backup.sh
./ops/restore.sh ./backups/<timestamp>
docker compose ps
docker compose logs backend
```

Production configuration is defined by `.env.example`. Secrets and local
environment files are excluded from version control.
