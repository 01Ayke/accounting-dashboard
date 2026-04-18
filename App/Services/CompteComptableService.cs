using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

public class CompteComptableService
{
    private readonly HttpClient _httpClient;

    public CompteComptableService()
    {
        _httpClient = new HttpClient { BaseAddress = new Uri("http://localhost:7070/") };
    }

    private void SetAuthHeader()
    {
        var token = SessionManager.Token;
        if (!string.IsNullOrEmpty(token))
            _httpClient.DefaultRequestHeaders.Authorization =
                new System.Net.Http.Headers.AuthenticationHeaderValue(token);
    }

    private static async Task<List<CompteDto>> ParseCompteDtos(HttpResponseMessage response)
    {
        var list = new List<CompteDto>();
        try
        {
            var body = await response.Content.ReadAsStringAsync();
            using var doc = JsonDocument.Parse(body);

            if (!doc.RootElement.TryGetProperty("message", out var msgEl) ||
                msgEl.ValueKind != JsonValueKind.Array)
                return list;

            foreach (var el in msgEl.EnumerateArray())
            {
                var dto = new CompteDto();

                if (el.TryGetProperty("idCompte", out var id) && id.ValueKind == JsonValueKind.Number)
                    dto.IdCompteApi = id.GetInt32();

                if (el.TryGetProperty("idCompteAuxiliaire", out var idAux) && idAux.ValueKind == JsonValueKind.Number)
                    dto.IdCompteAuxApi = idAux.GetInt32();

                if (el.TryGetProperty("numeroCompte", out var nr) && nr.ValueKind == JsonValueKind.String)
                    dto.NumeroCompteApi = nr.GetString();

                if (el.TryGetProperty("numeroCompteAuxiliaire", out var nrAux) && nrAux.ValueKind == JsonValueKind.String)
                    dto.NumeroCompteAuxApi = nrAux.GetString();

                if (el.TryGetProperty("libelle", out var lib) && lib.ValueKind == JsonValueKind.String)
                    dto.Libelle = lib.GetString() ?? "";

                list.Add(dto);
            }
        }
        catch { /* retourne liste vide si parsing échoue */ }
        return list;
    }

    // POST /compte-tiers
    public async Task<(bool Success, string Message)> CreateCompteAsync(CreateCompteRequest request)
    {
        try
        {
            SetAuthHeader();
            var response = await _httpClient.PostAsJsonAsync("compte-tiers", request);
            var body     = await response.Content.ReadAsStringAsync();
            if (response.IsSuccessStatusCode) return (true, "Compte créé.");
            using var doc = JsonDocument.Parse(body);
            var msg = doc.RootElement.TryGetProperty("message", out var m) ? m.GetString() : null;
            return (false, msg ?? $"Erreur {response.StatusCode}");
        }
        catch (Exception ex) { return (false, $"Connexion : {ex.Message}"); }
    }

    // POST /compte
    public async Task<(bool Success, string Message)> CreateCompteAuxiliaireAsync(CreateCompteAuxiliaireRequest request)
    {
        try
        {
            SetAuthHeader();
            var response = await _httpClient.PostAsJsonAsync("compte", request);
            var body     = await response.Content.ReadAsStringAsync();
            if (response.IsSuccessStatusCode) return (true, "Compte auxiliaire créé.");
            using var doc = JsonDocument.Parse(body);
            var msg = doc.RootElement.TryGetProperty("message", out var m) ? m.GetString() : null;
            return (false, msg ?? $"Erreur {response.StatusCode}");
        }
        catch (Exception ex) { return (false, $"Connexion : {ex.Message}"); }
    }

    // GET /dossier-comptes-agregation/{id} → Comptes 40x/41x (ex: 401, 411)
    public async Task<IReadOnlyList<CompteDto>> GetAllComptesAgregationAsync(int idDossier)
    {
        try
        {
            SetAuthHeader();
            return await ParseCompteDtos(await _httpClient.GetAsync($"dossier-comptes-agregation/{idDossier}"));
        }
        catch { return new List<CompteDto>(); }
    }

    // GET /dossier-comptes/{id} → tous les Comptes du dossier
    public async Task<IReadOnlyList<CompteDto>> GetAllComptesAsync(int idDossier)
    {
        try
        {
            SetAuthHeader();
            return await ParseCompteDtos(await _httpClient.GetAsync($"dossier-comptes/{idDossier}"));
        }
        catch { return new List<CompteDto>(); }
    }

    // GET /dossier-comptes-rattachement/{id} → CompteAuxiliaires 5 chiffres (ex: 40101)
    public async Task<IReadOnlyList<CompteDto>> GetAllCompteAuxiliairesAsync(int idDossier)
    {
        try
        {
            SetAuthHeader();
            return await ParseCompteDtos(await _httpClient.GetAsync($"dossier-comptes-rattachement/{idDossier}"));
        }
        catch { return new List<CompteDto>(); }
    }

    // GET /dossier-comptes-tiers/{id} → CompteAuxiliaires tiers 8 chiffres (ex: 40101001)
    public async Task<IReadOnlyList<CompteDto>> GetAllComptesTiersAsync(int idDossier)
    {
        try
        {
            SetAuthHeader();
            return await ParseCompteDtos(await _httpClient.GetAsync($"dossier-comptes-tiers/{idDossier}"));
        }
        catch { return new List<CompteDto>(); }
    }
}
