using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

/// <summary>
/// Gère les documents via DocumentRoutes.
/// Ces routes ne requièrent pas d'authentification (pas de checkToken dans le backend).
/// Attention : les réponses sont des objets Document directs, sans enveloppe status/message.
///
/// GET    /documents        → liste tous les documents
/// GET    /documents/{id}   → un document par id
/// POST   /documents        → crée un document
/// PUT    /documents/{id}   → met à jour un document
/// DELETE /documents/{id}   → supprime un document
/// </summary>
public class DocumentService
{
    private readonly HttpClient _httpClient;

    public DocumentService()
    {
        _httpClient = new HttpClient
        {
            BaseAddress = new Uri("http://localhost:7070/")
        };
    }

    // ─── GET /documents ────────────────────────────────────────────────────────
    // Réponse : tableau JSON de Document (sans enveloppe)
    public async Task<List<Document>> GetDocumentsAsync()
    {
        try
        {
            var json = await _httpClient.GetStringAsync("documents");
            var documents = JsonSerializer.Deserialize<List<Document>>(json) ?? new List<Document>();
            return documents;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DocumentService.GetDocumentsAsync: {ex.Message}");
            return new List<Document>();
        }
    }

    // ─── GET /documents/{id} ───────────────────────────────────────────────────
    // Réponse : objet Document direct, ou texte "Document non trouvé" (404)
    public async Task<Document?> GetDocumentAsync(int id)
    {
        try
        {
            var response = await _httpClient.GetAsync($"documents/{id}");
            if (!response.IsSuccessStatusCode)
            {
                System.Diagnostics.Debug.WriteLine($"DocumentService.GetDocumentAsync: {response.StatusCode}");
                return null;
            }
            var json = await response.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<Document>(json);
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DocumentService.GetDocumentAsync: {ex.Message}");
            return null;
        }
    }

    // ─── POST /documents ───────────────────────────────────────────────────────
    // Corps JSON : { "nom", "id_type_document", "dateDocument", "id_utilisateur" }
    // Réponse 201 : objet Document créé
    public async Task<(bool Success, Document? Created, string Error)> CreateDocumentAsync(Document document)
    {
        try
        {
            var response = await _httpClient.PostAsJsonAsync("documents", document);
            var body = await response.Content.ReadAsStringAsync();

            if (response.IsSuccessStatusCode)
            {
                var created = JsonSerializer.Deserialize<Document>(body);
                return (true, created, "");
            }

            return (false, null, $"Erreur {response.StatusCode}: {body}");
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DocumentService.CreateDocumentAsync: {ex.Message}");
            return (false, null, $"Erreur de connexion : {ex.Message}");
        }
    }

    // ─── PUT /documents/{id} ───────────────────────────────────────────────────
    // Corps JSON : mêmes champs que POST
    // Réponse 200 : objet Document mis à jour
    public async Task<(bool Success, Document? Updated, string Error)> UpdateDocumentAsync(int id, Document document)
    {
        try
        {
            var response = await _httpClient.PutAsJsonAsync($"documents/{id}", document);
            var body = await response.Content.ReadAsStringAsync();

            if (response.IsSuccessStatusCode)
            {
                var updated = JsonSerializer.Deserialize<Document>(body);
                return (true, updated, "");
            }

            return (false, null, $"Erreur {response.StatusCode}: {body}");
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DocumentService.UpdateDocumentAsync: {ex.Message}");
            return (false, null, $"Erreur de connexion : {ex.Message}");
        }
    }

    // ─── DELETE /documents/{id} ────────────────────────────────────────────────
    // Réponse 204 : sans body
    public async Task<(bool Success, string Error)> DeleteDocumentAsync(int id)
    {
        try
        {
            var response = await _httpClient.DeleteAsync($"documents/{id}");
            if (response.IsSuccessStatusCode)
                return (true, "");

            var body = await response.Content.ReadAsStringAsync();
            return (false, $"Erreur {response.StatusCode}: {body}");
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DocumentService.DeleteDocumentAsync: {ex.Message}");
            return (false, $"Erreur de connexion : {ex.Message}");
        }
    }
}
