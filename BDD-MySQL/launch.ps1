$ServiceName = "mysql-db"
$VolumeName = "mysql-data"

# Nom des fichiers de logs
$LogGeneral = "log-general.txt"
$LogError   = "log-error.txt"
$LogSlow    = "log-slow.txt"

Write-Host "Arrêt et suppression des conteneurs et volumes..."
docker-compose down -v

if (docker volume ls --format "{{.Name}}" | Select-String -Pattern $VolumeName) {
    Write-Host "Suppression forcée du volume $VolumeName..."
    docker volume rm $VolumeName
} else {
    Write-Host "Volume $VolumeName déjà supprimé."
}

Write-Host "Rebuild et relance du conteneur..."
docker-compose up -d --build

Write-Host "Attente que MySQL démarre (10 sec)..."
Start-Sleep -Seconds 10

# Récupération des logs
Write-Host "Récupération du general log dans $LogGeneral ..."
docker exec $ServiceName sh -c "test -f /var/lib/mysql/general.log && cat /var/lib/mysql/general.log || echo 'Pas de general log'" > $LogGeneral

Write-Host "Récupération du slow log dans $LogSlow ..."
docker exec $ServiceName sh -c "test -f /var/lib/mysql/mysql-slow.log && cat /var/lib/mysql/mysql-slow.log || echo 'Pas de slow log'" > $LogSlow

Write-Host "Récupération de l'error log dans $LogError ..."
docker exec $ServiceName sh -c "test -f /var/lib/mysql/mysql-error.log && cat /var/lib/mysql/mysql-error.log || echo 'Pas de error log'" > $LogError

Write-Host "Terminé. Tu peux consulter les logs dans $LogGeneral, $LogSlow et $LogError"
