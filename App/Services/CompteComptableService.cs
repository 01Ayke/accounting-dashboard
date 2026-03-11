using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

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

    public async Task<bool> CreateCompteAsync(CreateCompteRequest request)
    {
        try
        {
            var response = await _httpClient.PostAsJsonAsync("accounts", request);
            return response.IsSuccessStatusCode;
        }
        catch
        {
            return false;
        }
    }
}