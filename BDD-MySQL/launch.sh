#!/bin/bash

ServiceName="mysql-db"
VolumeName="mysql-data"

echo "Arrêt et suppression des conteneurs et volumes..."
# Supprime le container et tous les volumes définis dans le compose
docker-compose down -v

# Vérifier si le volume existe encore (sécurité)
if docker volume ls --format "{{.Name}}" | grep -q "^${VolumeName}$"; then
    echo "Suppression forcée du volume $VolumeName..."
    docker volume rm "$VolumeName"
else
    echo " Volume $VolumeName déjà supprimé."
fi

echo "Rebuild et relance du conteneur..."
docker-compose up -d --build

echo "Le conteneur $ServiceName a été relancé avec succès."
