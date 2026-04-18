using System.Text.Json.Serialization;

namespace App.Models;

public class CompteDto
{
    [JsonPropertyName("idCompte")]
    public int? IdCompteApi { get; set; }

    [JsonPropertyName("idCompteAuxiliaire")]
    public int? IdCompteAuxApi { get; set; }

    [JsonPropertyName("numeroCompte")]
    public string? NumeroCompteApi { get; set; }

    [JsonPropertyName("numeroCompteAuxiliaire")]
    public string? NumeroCompteAuxApi { get; set; }

    [JsonPropertyName("libelle")]
    public string Libelle { get; set; } = "";

    // Unified getters
    public int IdCompte => IdCompteApi ?? IdCompteAuxApi ?? 0;
    public string NumeroCompte => NumeroCompteApi ?? NumeroCompteAuxApi ?? "";

    public string DisplayText => $"{NumeroCompte} - {Libelle}";
}
