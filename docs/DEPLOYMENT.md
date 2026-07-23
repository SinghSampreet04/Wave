# Wave deployment guide

## Required configuration

Copy `.env.example` to `.env` at the repository root and replace all
placeholders. `JWT_SECRET` must be a base64-encoded secret of at least 64
random bytes:

```sh
openssl rand -base64 64
```

Set `PUBLIC_ORIGIN` to the exact browser origin, including `https://` and no
trailing slash. Set `DOMAIN` to the same host without the scheme and point its
DNS A/AAAA records at the VPS. Caddy obtains and renews HTTPS certificates
automatically. SMTP credentials are required for the production acceptance
check because verification and password reset are release features. Keep mail
out of readiness (`MAIL_HEALTH_ENABLED=false`) so a provider outage does not
restart otherwise healthy application containers.

## Start and verify

```sh
docker compose config
docker compose up --build -d
docker compose ps
curl --fail https://${DOMAIN}/healthz
curl --fail https://${DOMAIN}/actuator/health
```

The frontend health endpoint is `/healthz`. The backend health endpoint is
`/actuator/health/readiness` inside the backend container. `docker compose ps`
should report PostgreSQL, Redis, backend, and frontend healthy and Caddy
running.

## Edge and TLS

Caddy is the only public service and exposes TCP 80/443 plus UDP 443. The
frontend, backend, PostgreSQL, and Redis remain on the private Compose network.
The proxy chain preserves forwarded headers and WebSocket upgrades for `/ws`.

## Data and backups

The stack persists five named volumes:

- `postgres_data`: all relational application data
- `redis_data`: rate-limit and cache state
- `wave_uploads`: uploaded files
- `caddy_data`: TLS certificates and state
- `caddy_config`: Caddy runtime configuration

Create an application-consistent database/upload backup with:

```sh
./ops/backup.sh
```

Restore rehearsal must be performed before launch using a non-production copy:

```sh
./ops/restore.sh ./backups/<timestamp>
PUBLIC_ORIGIN=https://${DOMAIN} ./ops/smoke-test.sh
```

The restore command requires typing `RESTORE`. Redis data is not the source of
truth and can be rebuilt.

## Upgrade procedure

1. Run `./ops/backup.sh` and verify `SHA256SUMS`.
2. Build and validate the candidate images with `docker compose build`.
3. Run `npm run build && npm run lint` in `frontend`.
4. Run `mvn verify` in `backend`.
5. Deploy with `docker compose up -d --build`.
6. Run `./ops/smoke-test.sh`, then verify registration email, password-reset
   email, login, one channel message, one direct message, reaction toggling,
   attachment download, and a WebSocket reconnect with two browser sessions.

Flyway owns the production schema and Hibernate runs with `ddl-auto=validate`.
For a pre-Flyway database, the first startup baselines the existing schema at
version 1 and applies later migrations. Always take a verified backup before
that first conversion. A clean installation executes the complete V1 schema.

Rollback is image-first: retain the previous immutable image tags, stop the
candidate, restore the pre-upgrade database/upload backup if its migration was
not backward compatible, then start the prior Compose revision. Never manually
edit `flyway_schema_history`.

## Operational notes

- Rotate the JWT secret only with an explicit session-invalidation plan.
- Monitor container restarts, backend health, HTTP 5xx rate, JVM memory,
  PostgreSQL capacity, and upload volume usage.
- The in-process STOMP broker and presence registry target a single backend
  replica. Before horizontal backend scaling, move WebSocket fan-out and
  presence coordination to a shared broker/Redis pub-sub.
- Test SMTP delivery from the deployed network before enabling user onboarding.
- Production containers are memory-limited, use rotated JSON logs, and the
  application containers run with read-only root filesystems and
  `no-new-privileges`.
