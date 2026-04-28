#!/bin/bash
set -euo pipefail

ServiceName="mysql-db"
VolumeName="mysql-data"

# Nom des fichiers de logs
LogGeneral="log-general.txt"
LogError="log-error.txt"
LogSlow="log-slow.txt"

if docker compose version >/dev/null 2>&1; then
    ComposeCmd=(docker compose)
elif command -v docker-compose >/dev/null 2>&1; then
    ComposeCmd=(docker-compose)
else
    echo "Erreur: Docker Compose n'est pas installé." >&2
    exit 1
fi

wait_for_mysql() {
    local timeout=60
    local elapsed=0

    until docker exec "$ServiceName" sh -c 'mysqladmin ping -uroot -proot --silent' >/dev/null 2>&1; do
        if [ "$elapsed" -ge "$timeout" ]; then
            echo "Erreur: MySQL n'est pas disponible après ${timeout} secondes." >&2
            exit 1
        fi

        sleep 2
        elapsed=$((elapsed + 2))
    done
}

echo "Arrêt et suppression des conteneurs et volumes..."
"${ComposeCmd[@]}" down -v

if docker volume ls --format "{{.Name}}" | grep -q "$VolumeName"; then
    echo "Suppression forcée du volume $VolumeName..."
    docker volume rm "$VolumeName"
else
    echo "Volume $VolumeName déjà supprimé."
fi

echo "Rebuild et relance du conteneur..."
"${ComposeCmd[@]}" up -d --build

echo "Attente que MySQL soit disponible..."
wait_for_mysql

# Récupération des logs
echo "Récupération du general log dans $LogGeneral ..."
docker exec "$ServiceName" sh -c 'test -f /var/lib/mysql/general.log && cat /var/lib/mysql/general.log || echo "Pas de general log"' > "$LogGeneral"

echo "Récupération du slow log dans $LogSlow ..."
docker exec "$ServiceName" sh -c 'test -f /var/lib/mysql/mysql-slow.log && cat /var/lib/mysql/mysql-slow.log || echo "Pas de slow log"' > "$LogSlow"

echo "Récupération de l'error log dans $LogError ..."
docker exec "$ServiceName" sh -c 'test -f /var/lib/mysql/mysql-error.log && cat /var/lib/mysql/mysql-error.log || echo "Pas de error log"' > "$LogError"

echo "Terminé. Tu peux consulter les logs dans $LogGeneral, $LogSlow et $LogError"