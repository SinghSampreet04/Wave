#!/bin/sh
set -eu

origin=${PUBLIC_ORIGIN:?Export PUBLIC_ORIGIN before running this script}

curl --fail --silent --show-error "${origin}/healthz"
curl --fail --silent --show-error "${origin}/actuator/health"
curl --fail --silent --show-error "${origin}/api/v1/auth/refresh" \
  --request POST \
  --output /dev/null \
  --write-out "refresh endpoint HTTP %{http_code}\n"

docker compose ps
