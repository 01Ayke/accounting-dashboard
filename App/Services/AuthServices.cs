using App.Models;

namespace App.Services;

using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

public class AuthService
{
    private readonly HttpClient _httpClient;

    public AuthService()
    {
        _httpClient = new HttpClient
        {
            BaseAddress = new Uri("http://localhost:7070/")
        };
    }

    public async Task<LoginResponse?> LoginAsync(string email, string password)
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
}
