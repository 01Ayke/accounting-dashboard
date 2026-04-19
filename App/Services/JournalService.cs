using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

public class JournalService
{
    private readonly HttpClient _httpClient;

    public JournalService()
    {
        _httpClient = new HttpClient
        {
            BaseAddress = new Uri("http://localhost:7070/") // Addresse du backend Javalin
        };
    }

    private void SetAuthHeader()
    {
        if (!string.IsNullOrEmpty(SessionManager.Token))
        {
            _httpClient.DefaultRequestHeaders.Authorization =
                new System.Net.Http.Headers.AuthenticationHeaderValue(SessionManager.Token);
        }
    }

    public async Task<IReadOnlyList<JournalDto>> GetJournauxAsync(int dossierId)
    {
        var list = new List<JournalDto>();
        try
        {
            SetAuthHeader();
            var response = await _httpClient.GetAsync($"dossier-journaux/{dossierId}");
            var body = await response.Content.ReadAsStringAsync();
            using var doc = JsonDocument.Parse(body);
            if (!doc.RootElement.TryGetProperty("message", out var arr) ||
                arr.ValueKind != JsonValueKind.Array)
                return list;

            foreach (var el in arr.EnumerateArray())
            {
                var dto = new JournalDto();
                if (el.TryGetProperty("idJournal", out var id) && id.ValueKind == JsonValueKind.Number)
                    dto.Id = id.GetInt32();
                if (el.TryGetProperty("code", out var code) && code.ValueKind == JsonValueKind.String)
                    dto.Code = code.GetString() ?? "";
                if (el.TryGetProperty("libelle", out var lib) && lib.ValueKind == JsonValueKind.String)
                    dto.Libelle = lib.GetString() ?? "";
                list.Add(dto);
            }
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"JournalService.GetJournauxAsync: {ex.Message}");
        }
        return list;
    }

    public async Task<(bool Success, string Message)> CreateJournalAsync(int idDossier, int idTypeJournal, string code, string libelle, string commentaire)
    {
        try
        {
            SetAuthHeader();
            var request = new
            {
                idDossier = idDossier,
                idTypeJournal = idTypeJournal,
                code = code,
                libelle = libelle,
                commentaire = commentaire
            };

            var response = await _httpClient.PostAsJsonAsync("journal", request);
            var jsonStr = await response.Content.ReadAsStringAsync();

            if (response.IsSuccessStatusCode)
            {
                return (true, $"Journal « {libelle} » ({code}) créé avec succès.");
            }
            else
            {
                // Extract error message from JSON response
                string errorMessage = "Erreur de création";
                try
                {
                    using var doc = JsonDocument.Parse(jsonStr);
                    if (doc.RootElement.TryGetProperty("message", out var msgElement))
                    {
                        errorMessage = msgElement.GetString() ?? errorMessage;
                    }
                }
                catch { }

                return (false, errorMessage);
            }
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"JournalService.CreateJournalAsync: {ex.Message}");
            return (false, "Erreur de communication avec le serveur.");
        }
    }
}
