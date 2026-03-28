using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// Représente un document tel que renvoyé/attendu par le backend (DocumentRoutes).
/// Pas d'enveloppe status/message — le JSON est directement l'objet Document.
/// </summary>
public class Document
{
    [JsonPropertyName("id")]
    public int Id { get; set; }

    [JsonPropertyName("nom")]
    public string Nom { get; set; } = string.Empty;

    [JsonPropertyName("id_type_document")]
    public int IdTypeDocument { get; set; }

    [JsonPropertyName("dateDocument")]
    public string DateDocument { get; set; } = string.Empty;

    [JsonPropertyName("id_utilisateur")]
    public int IdUtilisateur { get; set; }
}
