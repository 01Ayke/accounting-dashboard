using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

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

    public async Task<bool> LogoutAsync()
    {
        var token = SessionManager.Token;
        if (string.IsNullOrEmpty(token))
            return false;

        try
        {
            _httpClient.DefaultRequestHeaders.Authorization =
                new System.Net.Http.Headers.AuthenticationHeaderValue(token);
            var response = await _httpClient.PostAsync("logout", null);
            return response.IsSuccessStatusCode;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"AuthService.LogoutAsync: {ex.Message}");
            return false;
        }
        finally
        {
            SessionManager.Clear();
        }
    }

    public async Task<LoginResponse?> LoginAsync(string email, string password)
    {
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
            var content = new FormUrlEncodedContent(new[]
            {
                new KeyValuePair<string, string>("mail", email),
                new KeyValuePair<string, string>("password", password)
            });

            System.Diagnostics.Debug.WriteLine($"AuthService: Envoi POST à login avec mail={email}");
            var response = await _httpClient.PostAsync("login", content);

            System.Diagnostics.Debug.WriteLine($"AuthService: Status code = {response.StatusCode}");

            var rawContent = await response.Content.ReadAsStringAsync();
            System.Diagnostics.Debug.WriteLine($"AuthService: Contenu brut reçu: {rawContent}");

            try
            {
                var loginResponse = await response.Content.ReadFromJsonAsync<LoginResponse>();
                System.Diagnostics.Debug.WriteLine($"AuthService: LoginResponse parsée - Status: {loginResponse?.Status}, Message: {loginResponse?.Message}, Token: {loginResponse?.Token}");
                return loginResponse;
            }
            catch (System.Text.Json.JsonException ex)
            {
                System.Diagnostics.Debug.WriteLine($"AuthService: Erreur JSON parsing: {ex.Message}");
                return new LoginResponse
                {
                    Status = "error",
                    Message = $"Erreur parsing JSON: {rawContent.Substring(0, Math.Min(200, rawContent.Length))}"
                };
            }
        }
        catch (HttpRequestException ex)
        {
            System.Diagnostics.Debug.WriteLine($"AuthService: HttpRequestException - {ex.Message}");
            return new LoginResponse
            {
                Status = "error",
                Message = $"Erreur de connexion au serveur : {ex.Message}"
            };
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"AuthService: Exception générale - {ex.GetType().Name}: {ex.Message}");
            return new LoginResponse
            {
                Status = "error",
                Message = $"Erreur : {ex.Message}"
            };
        }
    }
}