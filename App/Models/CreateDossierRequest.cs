using System.Text.Json.Serialization;

namespace App.Models;

/// <summary>
/// Corps de la requête POST /file — correspond exactement au modèle Java File.
/// </summary>
public class CreateDossierRequest
{
    [JsonPropertyName("denominationSociale")]
    public string DenominationSociale { get; set; } = "";

    [JsonPropertyName("telephone")]
    public string Telephone { get; set; } = "";

    [JsonPropertyName("mail")]
    public string Mail { get; set; } = "";

    [JsonPropertyName("capital")]
    public double Capital { get; set; }

    [JsonPropertyName("formeJuridique")]
    public FormeJuridiqueRef FormeJuridique { get; set; } = new();

    [JsonPropertyName("regimeFiscal")]
    public RegimeFiscalRef RegimeFiscal { get; set; } = new();

    [JsonPropertyName("regimeTVA")]
    public RegimeTvaRef RegimeTVA { get; set; } = new();

    [JsonPropertyName("adresse")]
    public AdresseRequest Adresse { get; set; } = new();
}

public class FormeJuridiqueRef
{
    [JsonPropertyName("idFormeJuridique")]
    public int IdFormeJuridique { get; set; }
}

public class RegimeFiscalRef
{
    [JsonPropertyName("idRegimeFiscal")]
    public int IdRegimeFiscal { get; set; }
}

public class RegimeTvaRef
{
    [JsonPropertyName("idRegimeTVA")]
    public int IdRegimeTVA { get; set; }
}

public class AdresseRequest
{
    [JsonPropertyName("pays")]
    public string Pays { get; set; } = "";

    [JsonPropertyName("ville")]
    public string Ville { get; set; } = "";

    [JsonPropertyName("rue")]
    public string Rue { get; set; } = "";

    [JsonPropertyName("numero")]
    public string Numero { get; set; } = "";

    [JsonPropertyName("complement")]
    public string Complement { get; set; } = "";

    [JsonPropertyName("codePostal")]
    public string CodePostal { get; set; } = "";
}
