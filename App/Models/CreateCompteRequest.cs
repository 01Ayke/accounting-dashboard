using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// Corps de la requête POST /compte-tiers.
/// Crée un Compte (compte principal) dans le plan comptable du dossier.
/// </summary>
public class CreateCompteRequest
{
    [JsonPropertyName("idDossier")]
    public int IdDossier { get; set; }

    [JsonPropertyName("numeroCompte")]
    public string NumeroCompte { get; set; } = "";

    [JsonPropertyName("libelle")]
    public string Libelle { get; set; } = "";
}

/// <summary>
/// Corps de la requête POST /compte.
/// Crée un CompteAuxiliaire rattaché à un Compte existant.
/// Le numéro doit être numérique et contenir le numéro du compte de rattachement.
/// </summary>
public class CreateCompteAuxiliaireRequest
{
    [JsonPropertyName("idTypeCompteAuxiliaire")]
    public int IdTypeCompteAuxiliaire { get; set; }

    [JsonPropertyName("idCompteRattachement")]
    public int IdCompteRattachement { get; set; }

    [JsonPropertyName("numero")]
    public string Numero { get; set; } = "";

    [JsonPropertyName("libelle")]
    public string Libelle { get; set; } = "";
}
