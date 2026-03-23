using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// DTO retourné par GET /regimeFiscal
/// </summary>
public class RegimeFiscal
{
    [JsonPropertyName("idRegimeFiscal")]
    public int IdRegimeFiscal { get; set; }

    [JsonPropertyName("libelle")]
    public string Libelle { get; set; } = "";

    public override string ToString() => Libelle;
}
