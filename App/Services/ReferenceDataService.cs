using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

/// <summary>
/// Récupère les données de référence depuis le backend :
/// GET /formeJuridique, GET /regimeFiscal, GET /regimeTVA
/// Ces endpoints ne nécessitent pas d'authentification.
/// </summary>
public class ReferenceDataService
{
    private readonly HttpClient _httpClient;

    private static readonly HttpClient _geoHttpClient = new HttpClient
    {
        BaseAddress = new Uri("https://geo.api.gouv.fr/")
    };

    public ReferenceDataService()
    {
        _httpClient = new HttpClient
        {
            BaseAddress = new Uri("http://localhost:7070/")
        };
    }

    public async Task<List<string>> GetCityByPostalCodeAsync(string postalCode)
    {
        try
        {
            var json = await _geoHttpClient.GetStringAsync($"communes?codePostal={postalCode}&fields=nom");
            using var doc = JsonDocument.Parse(json);
            var list = new List<string>();
            foreach (var item in doc.RootElement.EnumerateArray())
            {
                if (item.TryGetProperty("nom", out var nom))
                    list.Add(nom.GetString() ?? "");
            }
            return list;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"ReferenceDataService.GetCityByPostalCodeAsync: {ex.Message}");
            return new List<string>();
        }
    }

    public async Task<List<FormeJuridique>> GetFormesJuridiquesAsync()
    {
        try
        {
            var json = await _httpClient.GetStringAsync("formeJuridique");
            using var doc = JsonDocument.Parse(json);
            var message = doc.RootElement.GetProperty("message");
            var list = new List<FormeJuridique>();
            foreach (var item in message.EnumerateArray())
            {
                list.Add(new FormeJuridique
                {
                    IdFormeJuridique = item.GetProperty("idFormeJuridique").GetInt32(),
                    Libelle = item.TryGetProperty("libelle", out var lib) ? lib.GetString() ?? "" : $"Forme {item.GetProperty("idFormeJuridique").GetInt32()}"
                });
            }
            return list;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"ReferenceDataService.GetFormesJuridiquesAsync: {ex.Message}");
            return new List<FormeJuridique>();
        }
    }

    public async Task<List<RegimeFiscal>> GetRegimesFiscauxAsync()
    {
        try
        {
            var json = await _httpClient.GetStringAsync("regimeFiscal");
            using var doc = JsonDocument.Parse(json);
            var message = doc.RootElement.GetProperty("message");
            var list = new List<RegimeFiscal>();
            foreach (var item in message.EnumerateArray())
            {
                list.Add(new RegimeFiscal
                {
                    IdRegimeFiscal = item.GetProperty("idRegimeFiscal").GetInt32(),
                    Libelle = item.TryGetProperty("libelle", out var lib) ? lib.GetString() ?? "" : $"Régime {item.GetProperty("idRegimeFiscal").GetInt32()}"
                });
            }
            return list;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"ReferenceDataService.GetRegimesFiscauxAsync: {ex.Message}");
            return new List<RegimeFiscal>();
        }
    }

    public async Task<List<RegimeTva>> GetRegimesTVAAsync()
    {
        try
        {
            var json = await _httpClient.GetStringAsync("regimeTVA");
            using var doc = JsonDocument.Parse(json);
            var message = doc.RootElement.GetProperty("message");
            var list = new List<RegimeTva>();
            foreach (var item in message.EnumerateArray())
            {
                list.Add(new RegimeTva
                {
                    IdRegimeTVA = item.GetProperty("idRegimeTVA").GetInt32(),
                    Libelle = item.TryGetProperty("libelle", out var lib) ? lib.GetString() ?? "" : $"TVA {item.GetProperty("idRegimeTVA").GetInt32()}"
                });
            }
            return list;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"ReferenceDataService.GetRegimesTVAAsync: {ex.Message}");
            return new List<RegimeTva>();
        }
    }
}
