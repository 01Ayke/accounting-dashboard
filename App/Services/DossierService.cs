using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

public class DossierService
{
    private readonly HttpClient _httpClient;

    public DossierService()
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

    // ─── GET /files ────────────────────────────────────────────────────────────
    public async Task<List<DossierFactice>> GetTousLesDossiersAsync()
    {
        try
        {
            System.Diagnostics.Debug.WriteLine("DossierService.GetTousLesDossiersAsync: Appel GET /files");
            var json = await _httpClient.GetStringAsync("files");

            using var doc = JsonDocument.Parse(json);
            var root = doc.RootElement;
            var filesArray = root.GetProperty("message");

            var dossiers = System.Text.Json.JsonSerializer.Deserialize<List<DossierFactice>>(
                filesArray.GetRawText(),
                new System.Text.Json.JsonSerializerOptions { PropertyNameCaseInsensitive = true }
            ) ?? new List<DossierFactice>();

            return dossiers;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.GetTousLesDossiersAsync: Exception = {ex.Message}");
            return new List<DossierFactice>();
        }
    }


    // ─── GET /files/{id} ──────────────────────────────────────────────────────
    public async Task<DossierFactice?> GetDossierFileAsync(int dossierId)
    {
        try
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.GetDossierFileAsync: GET /files/{dossierId}");
            var response = await _httpClient.GetAsync($"files/{dossierId}");
            if (!response.IsSuccessStatusCode) return null;

            var json = await response.Content.ReadAsStringAsync();
            var dossier = System.Text.Json.JsonSerializer.Deserialize<DossierFactice>(
                json,
                new System.Text.Json.JsonSerializerOptions { PropertyNameCaseInsensitive = true }
            );
            return dossier;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.GetDossierFileAsync: Exception = {ex.Message}");
            return null;
        }
    }

    // ─── GET /file/{id} (dashboard) ────────────────────────────────────────────
    // Authentification requise.
    // Réponse : { "status": "success", "message": { "file": {...}, "sig": {...}, "turnover": [...] } }
    public async Task<DossierFactice?> GetDossierAsync(int dossierId)
    {
        try
        {
            SetAuthHeader();
            System.Diagnostics.Debug.WriteLine($"DossierService.GetDossierAsync: GET /file/{dossierId}");

            var response = await _httpClient.GetAsync($"file/{dossierId}");
            if (!response.IsSuccessStatusCode)
            {
                System.Diagnostics.Debug.WriteLine($"DossierService.GetDossierAsync: Erreur {response.StatusCode}");
                return null;
            }

            var json = await response.Content.ReadAsStringAsync();
            using var doc = JsonDocument.Parse(json);
            var fileObj = doc.RootElement.GetProperty("message").GetProperty("file");

            string prenom = "", nom = "", emailRepresentant = "";
            if (fileObj.TryGetProperty("representants", out var representants) && representants.GetArrayLength() > 0)
            {
                var firstRep = representants[0];
                prenom = firstRep.TryGetProperty("prenom", out var p) ? p.GetString() ?? "" : "";
                nom    = firstRep.TryGetProperty("nom",    out var n) ? n.GetString() ?? "" : "";
                emailRepresentant = firstRep.TryGetProperty("mail", out var m) ? m.GetString() ?? "" : "";
            }

            return new DossierFactice
            {
                IdDossier           = dossierId,
                DenominationSociale = fileObj.GetProperty("denominationSociale").GetString() ?? "",
                Secteur             = "Comptabilité",
                Statut              = "Actif"
            };
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.GetDossierAsync: Exception = {ex.Message}");
            return null;
        }
    }

    // ─── GET /file/{id} → SIG ──────────────────────────────────────────────────
    public async Task<SIGBackend?> GetSIGAsync(int dossierId)
    {
        try
        {
            SetAuthHeader();
            var response = await _httpClient.GetAsync($"file/{dossierId}");
            if (!response.IsSuccessStatusCode) return null;

            var json = await response.Content.ReadAsStringAsync();
            using var doc = JsonDocument.Parse(json);
            var fileJson = doc.RootElement.GetProperty("message");

            if (!fileJson.TryGetProperty("sig", out var sigObj)) return null;

            return new SIGBackend
            {
                MargeCommerciale    = sigObj.TryGetProperty("margeCommerciale",    out var mc)   ? mc.GetDouble()  : double.NaN,
                ProductionExercice  = sigObj.TryGetProperty("productionExercice",  out var pe)   ? pe.GetDouble()  : double.NaN,
                ValeurAjoutee       = sigObj.TryGetProperty("valeurAjoutee",       out var va)   ? va.GetDouble()  : double.NaN,
                ExcedentBrut        = sigObj.TryGetProperty("excedentBrut",        out var eb)   ? eb.GetDouble()  : double.NaN,
                ResultatExploitation = sigObj.TryGetProperty("resultatExploitation", out var re) ? re.GetDouble()  : double.NaN,
                ResultatFinancier   = sigObj.TryGetProperty("resultatFinancier",   out var rf)   ? rf.GetDouble()  : double.NaN,
                ResultatExceptionnel= sigObj.TryGetProperty("resultatExceptionnel", out var rex) ? rex.GetDouble() : double.NaN,
                ResultatExercice    = sigObj.TryGetProperty("resultatExercice",    out var rexe) ? rexe.GetDouble(): double.NaN
            };
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.GetSIGAsync: Exception = {ex.Message}");
            return null;
        }
    }

    // ─── POST /file ────────────────────────────────────────────────────────────
    // Authentification requise.
    // Réponse : { "status": "ok", "message": "Dossier créé" }
    public async Task<(bool Success, string Message)> CreateDossierAsync(CreateDossierRequest request)
    {
        try
        {
            SetAuthHeader();
            System.Diagnostics.Debug.WriteLine("DossierService.CreateDossierAsync: POST /file");

            var response = await _httpClient.PostAsJsonAsync("file", request);
            var body = await response.Content.ReadAsStringAsync();
            System.Diagnostics.Debug.WriteLine($"DossierService.CreateDossierAsync: {response.StatusCode} — {body}");

            if (response.IsSuccessStatusCode)
                return (true, "Dossier créé avec succès.");

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
            System.Diagnostics.Debug.WriteLine($"DossierService.CreateDossierAsync: Exception = {ex.Message}");
            return (false, $"Erreur de connexion : {ex.Message}");
        }
    }

    // ─── POST /affectations ────────────────────────────────────────────────────
    // Authentification requise.
    // Corps : { "idUser": int, "idDossier": int }
    // Réponse : { "status": "ok", "message": "Dossier attribué avec succès" }
    public async Task<(bool Success, string Message)> AssignDossierAsync(AssignDossierRequest request)
    {
        try
        {
            SetAuthHeader();
            System.Diagnostics.Debug.WriteLine($"DossierService.AssignDossierAsync: POST /affectations idUser={request.IdUser} idDossier={request.IdDossier}");

            var response = await _httpClient.PostAsJsonAsync("affectations", request);
            var body = await response.Content.ReadAsStringAsync();
            System.Diagnostics.Debug.WriteLine($"DossierService.AssignDossierAsync: {response.StatusCode} — {body}");

            if (response.IsSuccessStatusCode)
                return (true, "Dossier attribué avec succès.");

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
            System.Diagnostics.Debug.WriteLine($"DossierService.AssignDossierAsync: Exception = {ex.Message}");
            return (false, $"Erreur de connexion : {ex.Message}");
        }
    }

    // ─── POST /affectations/revoke ─────────────────────────────────────────────
    public async Task<(bool Success, string Message)> RevokeDossierAsync(AssignDossierRequest request)
    {
        try
        {
            SetAuthHeader();
            var response = await _httpClient.PostAsJsonAsync("affectations/revoke", request);
            var body = await response.Content.ReadAsStringAsync();
            return response.IsSuccessStatusCode
                ? (true, "Affectation révoquée.")
                : (false, $"Erreur {response.StatusCode}");
        }
        catch (Exception ex)
        {
            return (false, $"Erreur de connexion : {ex.Message}");
        }
    }
}
