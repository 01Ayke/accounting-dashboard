using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// Corps de la requête POST /compte.
/// Backend attend : { idDossier, numero, libelle }
/// </summary>
public class CreateCompteRequest
{
    [JsonPropertyName("idDossier")]
    public int IdDossier { get; set; }

    [JsonPropertyName("numero")]
    public string Numero { get; set; } = "";

    [JsonPropertyName("libelle")]
    public string Libelle { get; set; } = "";
}
