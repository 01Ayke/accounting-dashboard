# reset_mysql.ps1
$ServiceName = "mysql-db"
$VolumeName = "mysql-data"

Write-Host "Arrêt et suppression des conteneurs et volumes..."
# Supprime le container ET le volume en un seul coup
docker-compose down -v

# Optionnel : forcer la suppression si le volume traîne encore
if (docker volume ls --format "{{.Name}}" | Select-String -Pattern $VolumeName) {
    Write-Host "Suppression forcée du volume $VolumeName..."
    docker volume rm $VolumeName
} else {
    Write-Host "Volume $VolumeName déjà supprimé."
}

Write-Host "Rebuild et relance du conteneur..."
docker-compose up -d --build

Write-Host "Le conteneur $ServiceName a été relancé avec succès."
