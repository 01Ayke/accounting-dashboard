using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// DTO retourné par GET /formeJuridique
/// </summary>
public class FormeJuridique
{
    [JsonPropertyName("idFormeJuridique")]
    public int IdFormeJuridique { get; set; }

    [JsonPropertyName("libelle")]
    public string Libelle { get; set; } = "";

    public override string ToString() => Libelle;
}
