namespace App.Models;


public class DossierFactice
{
    public int IdDossier { get; set; }
    public required string NomEntreprise { get; set; }
    public required string Siret { get; set; }

    public required string Prenom { get; set; }
    public required string Nom { get; set; }
    public required string EmailContact { get; set; }

    public string Secteur { get; set; } = "";
    public string Statut { get; set; } = "Actif";

    public string PrenomNomContact => $"{Prenom} {Nom}";
}
