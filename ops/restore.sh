#!/bin/sh
set -eu

backup_dir=${1:?Usage: ./ops/restore.sh BACKUP_DIRECTORY}

test -f "${backup_dir}/wave.dump"
test -f "${backup_dir}/uploads.tar.gz"

echo "This replaces the Wave database and uploads with ${backup_dir}."
printf "Type RESTORE to continue: "
read -r confirmation
test "${confirmation}" = "RESTORE"

(cd "${backup_dir}" && sha256sum -c SHA256SUMS)

docker compose stop backend frontend caddy
docker compose exec -T postgres dropdb \
  --username "${POSTGRES_USER:-wave}" \
  --if-exists "${POSTGRES_DB:-wave}"
docker compose exec -T postgres createdb \
  --username "${POSTGRES_USER:-wave}" \
  "${POSTGRES_DB:-wave}"
docker compose exec -T postgres pg_restore \
  --username "${POSTGRES_USER:-wave}" \
  --dbname "${POSTGRES_DB:-wave}" \
  --clean --if-exists < "${backup_dir}/wave.dump"

docker run --rm \
  --volume wave_wave_uploads:/target \
  --volume "$(cd "${backup_dir}" && pwd):/backup:ro" \
  alpine:3.22 \
  sh -c 'find /target -mindepth 1 -delete && tar -C /target -xzf /backup/uploads.tar.gz'

docker compose up -d
echo "Restore complete. Run ./ops/smoke-test.sh after services are healthy."
