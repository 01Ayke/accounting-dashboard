using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// DTO retourné par GET /regimeTVA
/// </summary>
public class RegimeTva
{
    [JsonPropertyName("idRegimeTVA")]
    public int IdRegimeTVA { get; set; }

    [JsonPropertyName("libelle")]
    public string Libelle { get; set; } = "";

    public override string ToString() => Libelle;
}
