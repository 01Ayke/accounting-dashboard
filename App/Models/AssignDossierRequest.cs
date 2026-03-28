using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// Corps de la requête POST /affectations.
/// Backend attend : { idUser, idDossier }
/// </summary>
public class AssignDossierRequest
{
    [JsonPropertyName("idUser")]
    public int IdUser { get; set; }

    [JsonPropertyName("idDossier")]
    public int IdDossier { get; set; }
}
