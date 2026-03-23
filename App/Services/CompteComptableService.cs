using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

/// <summary>
/// Gère les comptes comptables auxiliaires.
///
/// Endpoint : POST /compte  (authentification requise)
/// Corps JSON : { "idDossier": int, "numero": "string", "libelle": "string" }
/// Réponse    : { "status": "ok", "message": "Compte auxiliaire créé" }
/// </summary>
public class CompteComptableService
{
    private readonly HttpClient _httpClient;

    public CompteComptableService()
    {
        _httpClient = new HttpClient
        {
            BaseAddress = new Uri("http://localhost:7070/")
        };
    }

    private void SetAuthHeader()
    {
        var token = SessionManager.Token;
        if (!string.IsNullOrEmpty(token))
            _httpClient.DefaultRequestHeaders.Authorization =
                new System.Net.Http.Headers.AuthenticationHeaderValue(token);
    }

    public async Task<(bool Success, string Message)> CreateCompteAsync(CreateCompteRequest request)
    {
        try
        {
            SetAuthHeader();
            System.Diagnostics.Debug.WriteLine($"CompteComptableService.CreateCompteAsync: POST /compte idDossier={request.IdDossier}");

            var response = await _httpClient.PostAsJsonAsync("compte", request);
            var body = await response.Content.ReadAsStringAsync();
            System.Diagnostics.Debug.WriteLine($"CompteComptableService: {response.StatusCode} — {body}");

            if (response.IsSuccessStatusCode)
                return (true, "Compte auxiliaire créé avec succès.");

            try
            {
                using var doc = JsonDocument.Parse(body);
                var msg = doc.RootElement.TryGetProperty("message", out var m) ? m.GetString() : null;
                return (false, msg ?? $"Erreur {response.StatusCode}");
            }
            catch { return (false, $"Erreur {response.StatusCode}"); }
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"CompteComptableService: Exception = {ex.Message}");
            return (false, $"Erreur de connexion : {ex.Message}");
        }
    }
}
