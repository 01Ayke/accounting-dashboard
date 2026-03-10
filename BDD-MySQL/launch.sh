#!/bin/bash

ServiceName="mysql-db"
VolumeName="mysql-data"

# Nom des fichiers de logs
LogGeneral="log-general.txt"
LogError="log-error.txt"
LogSlow="log-slow.txt"

echo "Arrêt et suppression des conteneurs et volumes..."
docker-compose down -v

if docker volume ls --format "{{.Name}}" | grep -q "$VolumeName"; then
    echo "Suppression forcée du volume $VolumeName..."
    docker volume rm "$VolumeName"
else
    echo "Volume $VolumeName déjà supprimé."
fi

echo "Rebuild et relance du conteneur..."
docker-compose up -d --build

echo "Attente que MySQL démarre (10 sec)..."
sleep 10

# Récupération des logs
echo "Récupération du general log dans $LogGeneral ..."
docker exec "$ServiceName" sh -c 'test -f /var/lib/mysql/general.log && cat /var/lib/mysql/general.log || echo "Pas de general log"' > "$LogGeneral"

echo "Récupération du slow log dans $LogSlow ..."
docker exec "$ServiceName" sh -c 'test -f /var/lib/mysql/mysql-slow.log && cat /var/lib/mysql/mysql-slow.log || echo "Pas de slow log"' > "$LogSlow"

echo "Récupération de l'error log dans $LogError ..."
docker exec "$ServiceName" sh -c 'test -f /var/lib/mysql/mysql-error.log && cat /var/lib/mysql/mysql-error.log || echo "Pas de error log"' > "$LogError"

echo "Terminé. Tu peux consulter les logs dans $LogGeneral, $LogSlow et $LogError"