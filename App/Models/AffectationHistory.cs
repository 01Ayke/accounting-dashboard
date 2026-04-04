using System;
using System.Linq;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace App.Models;

public class AffectationHistory
{
    [JsonPropertyName("idAffectation")]
    public int IdAffectation { get; set; }

    [JsonPropertyName("idUser")]
    public int IdUser { get; set; }

    [JsonPropertyName("nomUser")]
    public string NomUser { get; set; } = string.Empty;

    [JsonPropertyName("prenomUser")]
    public string PrenomUser { get; set; } = string.Empty;

    [JsonPropertyName("idDossier")]
    public int IdDossier { get; set; }

    [JsonPropertyName("nomDossier")]
    public string NomDossier { get; set; } = string.Empty;

    [JsonPropertyName("dateAffectation")]
    public JsonElement? DateAffectationRaw { get; set; }

    [JsonPropertyName("dateFinAffectation")]
    public JsonElement? DateFinAffectationRaw { get; set; }

    [JsonPropertyName("actif")]
    public bool Actif { get; set; }

    [JsonIgnore]
    public string StatutDisplay => Actif ? "Actif" : "Révoqué";

    [JsonIgnore]
    public string DateAffectationDisplay => FormatDate(DateAffectationRaw);

    [JsonIgnore]
    public string DateFinAffectationDisplay => FormatDate(DateFinAffectationRaw);

    private string FormatDate(JsonElement? el)
    {
        if (!el.HasValue || el.Value.ValueKind == JsonValueKind.Null) return "-";
        var val = el.Value;

        if (val.ValueKind == JsonValueKind.Number)
        {
            return DateTimeOffset.FromUnixTimeMilliseconds(val.GetInt64()).ToString("dd/MM/yyyy HH:mm");
        }
        else if (val.ValueKind == JsonValueKind.String)
        {
            if (DateTime.TryParse(val.GetString(), out var dt))
                return dt.ToString("dd/MM/yyyy HH:mm");
        }
        else if (val.ValueKind == JsonValueKind.Array)
        {
            var arr = val.EnumerateArray().ToList();
            if (arr.Count >= 3)
                return $"{arr[2].GetInt32():00}/{arr[1].GetInt32():00}/{arr[0].GetInt32():0000}";
        }

        return "-";
    }
}
