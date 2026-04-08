using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// Représente un dossier tel que retourné par GET /files du backend.
/// Les noms de propriétés correspondent exactement aux champs JSON du backend Java.
/// </summary>
public class DossierFactice
{
    [JsonPropertyName("id")]
    public int IdDossier { get; set; }

    [JsonPropertyName("denominationSociale")]
    public string DenominationSociale { get; set; } = "";

    [JsonPropertyName("telephone")]
    public string Telephone { get; set; } = "";

    [JsonPropertyName("mail")]
    public string Mail { get; set; } = "";

    [JsonPropertyName("capital")]
    public float Capital { get; set; }

    [JsonPropertyName("representants")]
    public List<Representant> Representants { get; set; } = new();

    public string Secteur { get; set; } = "Comptabilité";
    public string Statut { get; set; } = "Actif";

    [JsonIgnore]
    public string PrenomNomContact => Representants.Count > 0
        ? $"{Representants[0].Prenom} {Representants[0].Nom}"
        : "";

    [JsonIgnore]
    public string EmailContact => Representants.Count > 0
        ? Representants[0].Mail
        : "";
}
