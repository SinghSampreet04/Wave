# Wave

Wave is a real-time collaboration app with a React/Vite frontend and a Spring
Boot backend. It supports account creation, workspaces, channels, messaging,
threads, reactions, WebSocket updates, invitations, notifications, and file
attachments.

## Run locally

Requirements: Node.js 20+, Java 17+, and Docker Desktop.

1. Prepare the local service configuration:

   ```sh
   cp backend/.env.example backend/.env
   ```

2. Start PostgreSQL, Redis, and the backend:

   ```sh
   cd backend
   docker compose --env-file .env up --build
   ```

3. In another terminal, start the frontend:

   ```sh
   cd frontend
   cp .env.example .env
   npm ci
   npm run dev
   ```

Open http://localhost:5173. The API is available on port 8082 and its health
endpoint is http://localhost:8082/actuator/health.

## Deploy with Docker Compose

The root Compose stack builds and runs the React application, Spring Boot API,
PostgreSQL, and Redis behind one browser origin.

```sh
cp .env.example .env
# Replace every placeholder in .env, especially POSTGRES_PASSWORD,
# JWT_SECRET, and PUBLIC_ORIGIN.
docker compose up --build -d
docker compose ps
```

Wave is exposed on `APP_PORT` (8080 by default). In a public environment,
terminate TLS in a load balancer or reverse proxy and set `PUBLIC_ORIGIN` to
the exact HTTPS origin. PostgreSQL and Redis are kept on an internal Docker
network; database, Redis, and uploaded-file data use named volumes.

See [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md) for the production checklist,
backup guidance, health checks, and upgrade procedure.

## Checks

```sh
cd frontend && npm run build && npm run lint
cd backend && ./mvnw test
```

`backend/.env` and `frontend/.env` are local-only files. Use unique database
passwords and JWT secrets outside local development; do not commit real email
credentials or production secrets.

## Completed release flows

- Auth, verification, password recovery, workspaces, channels, invitations
- Real-time channel messages, edits, deletes, reactions, typing, and threads
- Direct conversations with send, edit, delete, reaction, and live updates
- Access-controlled channel and direct-message search
- Real-time notification center with read state
- Pins, mentions, read receipts, file attachments, and presence infrastructure
- Rate limiting, Actuator health, Prometheus metrics, persistent uploads
- Production containers, same-origin WebSocket/API proxying, health checks,
  graceful shutdown, and non-root application runtimes
