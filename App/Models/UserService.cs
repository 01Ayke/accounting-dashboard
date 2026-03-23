using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

/// <summary>
/// Gestion des utilisateurs.
/// POST /register  → création (form params, nécessite ADMIN)
/// POST /role-manager/{id} → changement de rôle (form params, nécessite ADMIN)
/// GET  /role-manager      → liste des utilisateurs
/// </summary>
public class UserService
{
    private readonly HttpClient _httpClient;

    public UserService()
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

    /// <summary>
    /// Crée un compte via POST /register (form-encoded, rôle ADMIN requis).
    /// </summary>
    public async Task<(bool Success, string Message)> CreateUserAsync(CreateUserRequest request)
    {
        try
        {
            SetAuthHeader();

            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("mail",     request.Email),
                new KeyValuePair<string, string>("password", request.Password),
                new KeyValuePair<string, string>("nom",      request.Nom),
                new KeyValuePair<string, string>("prenom",   request.Prenom),
                new KeyValuePair<string, string>("code",     request.Code),
            });

            var response = await _httpClient.PostAsync("register", content);
            var body = await response.Content.ReadAsStringAsync();
            System.Diagnostics.Debug.WriteLine($"UserService.CreateUser: {response.StatusCode} — {body}");

            if (response.IsSuccessStatusCode)
                return (true, "Compte créé avec succès.");

            // Essayer d'extraire le message d'erreur du backend
            try
            {
                using var doc = JsonDocument.Parse(body);
                var msg = doc.RootElement.TryGetProperty("message", out var m) ? m.GetString() : null;
                return (false, msg ?? $"Erreur {response.StatusCode}");
            }
            catch
            {
                return (false, $"Erreur {response.StatusCode}");
            }
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"UserService.CreateUser: Exception — {ex.Message}");
            return (false, $"Erreur de connexion : {ex.Message}");
        }
    }

    /// <summary>
    /// Attribue des rôles via POST /role-manager/{id} (form params, rôle ADMIN requis).
    /// Le paramètre roles est une chaîne CSV, ex. "ADMIN,USER".
    /// </summary>
    public async Task<(bool Success, string Message)> AssignRoleAsync(int userId, string roles)
    {
        try
        {
            SetAuthHeader();

            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("roles", roles)
            });

            var response = await _httpClient.PostAsync($"role-manager/{userId}", content);
            var body = await response.Content.ReadAsStringAsync();
            System.Diagnostics.Debug.WriteLine($"UserService.AssignRole: {response.StatusCode} — {body}");

            if (response.IsSuccessStatusCode)
                return (true, "Rôle attribué avec succès.");

            try
            {
                using var doc = JsonDocument.Parse(body);
                var msg = doc.RootElement.TryGetProperty("message", out var m) ? m.GetString() : null;
                return (false, msg ?? $"Erreur {response.StatusCode}");
            }
            catch
            {
                return (false, $"Erreur {response.StatusCode}");
            }
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"UserService.AssignRole: Exception — {ex.Message}");
            return (false, $"Erreur de connexion : {ex.Message}");
        }
    }

    /// <summary>
    /// Récupère les rôles d'un utilisateur via GET /role/{id} (rôle ADMIN requis).
    /// </summary>
    public async Task<List<string>> GetUserRolesAsync(int userId)
    {
        try
        {
            SetAuthHeader();
            var json = await _httpClient.GetStringAsync($"role/{userId}");
            using var doc = JsonDocument.Parse(json);
            var data = doc.RootElement.GetProperty("data");
            var roles = new List<string>();
            foreach (var item in data.EnumerateArray())
                roles.Add(item.GetString() ?? "");
            return roles;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"UserService.GetUserRoles: Exception — {ex.Message}");
            return new List<string>();
        }
    }

    /// <summary>
    /// Récupère la liste des utilisateurs via GET /role-manager.
    /// </summary>
    public async Task<List<UserItem>> GetUsersAsync()
    {
        try
        {
            SetAuthHeader();
            var json = await _httpClient.GetStringAsync("role-manager");
            using var doc = JsonDocument.Parse(json);
            var data = doc.RootElement.GetProperty("data");
            var users = new List<UserItem>();
            foreach (var item in data.EnumerateArray())
            {
                users.Add(new UserItem
                {
                    Id = item.TryGetProperty("id", out var id) ? id.GetInt32() : 0,
                    Mail = item.TryGetProperty("mail", out var mail) ? mail.GetString() ?? "" : ""
                });
            }
            return users;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"UserService.GetUsers: Exception — {ex.Message}");
            return new List<UserItem>();
        }
    }
}

public class UserItem
{
    public int Id { get; set; }
    public string Mail { get; set; } = "";
    public override string ToString() => $"[{Id}] {Mail}";
}
