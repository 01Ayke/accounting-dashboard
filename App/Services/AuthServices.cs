using App.Models;

namespace App.Services;

using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

public class AuthService
{
    private readonly HttpClient _httpClient;
    private readonly bool _useTestMode;

    public AuthService(bool useTestMode = false)
    {
        _useTestMode = useTestMode;
        _httpClient = new HttpClient
        {
            BaseAddress = new Uri("http://localhost:7070/")
        };
    }

    public async Task<LoginResponse?> LoginAsync(string email, string password)
    {
        // Mode test : retourne une réponse hardcodée sans faire de requête HTTP
        if (_useTestMode)
        {
            return await Task.FromResult(new LoginResponse
            {
                Status = "success",
                Message = "Login successful (TEST MODE)"
            });
        }

        try
        {
            var response = await _httpClient.PostAsJsonAsync("login", new
            {
                mail = email,      // ⚠️ IMPORTANT : ton backend attend "mail"
                password = password
            });

            if (!response.IsSuccessStatusCode)
            {
                return await response.Content.ReadFromJsonAsync<LoginResponse>();
            }

            return await response.Content.ReadFromJsonAsync<LoginResponse>();
        }
        catch (HttpRequestException ex)
        {
            return new LoginResponse
            {
                Status = "error",
                Message = $"Erreur de connexion au serveur : {ex.Message}"
            };
        }
    }
}
