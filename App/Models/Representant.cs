using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// Représentant tel que retourné par l'API backend (table representant).
/// </summary>
public class Representant
{
    [JsonPropertyName("id_representant")]
    public int Id { get; set; }

    [JsonPropertyName("nom")]
    public string Nom { get; set; } = "";

    [JsonPropertyName("prenom")]
    public string Prenom { get; set; } = "";

    [JsonPropertyName("mail")]
    public string Mail { get; set; } = "";

    [JsonPropertyName("telephone")]
    public string Telephone { get; set; } = "";

    [JsonPropertyName("qualite")]
    public string Qualite { get; set; } = "";
}