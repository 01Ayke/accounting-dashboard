using System;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

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

    public async Task<bool> CreateUserAsync(CreateUserRequest request)
    {
        try
        {
            var response = await _httpClient.PostAsJsonAsync("users", request);
            return response.IsSuccessStatusCode;
        }
        catch
        {
            return false;
        }
    }

    public async Task<bool> AssignRoleAsync(int userId, AssignRoleRequest request)
    {
        try
        {
            var response = await _httpClient.PutAsJsonAsync($"users/{userId}/role", request);
            return response.IsSuccessStatusCode;
        }
        catch
        {
            return false;
        }
    }
}