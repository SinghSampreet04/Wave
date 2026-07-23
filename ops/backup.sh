#!/bin/sh
set -eu

backup_root=${1:-./backups}
timestamp=$(date -u +%Y%m%dT%H%M%SZ)
backup_dir="${backup_root}/${timestamp}"

mkdir -p "${backup_dir}"

docker compose exec -T postgres pg_dump \
  --username "${POSTGRES_USER:-wave}" \
  --dbname "${POSTGRES_DB:-wave}" \
  --format custom \
  > "${backup_dir}/wave.dump"

docker run --rm \
  --volume wave_wave_uploads:/source:ro \
  --volume "$(cd "${backup_dir}" && pwd):/backup" \
  alpine:3.22 \
  tar -C /source -czf /backup/uploads.tar.gz .

sha256sum "${backup_dir}/wave.dump" "${backup_dir}/uploads.tar.gz" \
  > "${backup_dir}/SHA256SUMS"

echo "Backup written to ${backup_dir}"
