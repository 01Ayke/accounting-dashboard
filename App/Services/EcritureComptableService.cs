using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;

namespace App.Services;

public class EcritureComptableService
{
    private readonly HttpClient _httpClient;

    public EcritureComptableService()
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

    /// <summary>
    /// Envoie un batch d'écritures.
    /// Lignes est une liste de Tuple (IdCompte, Debit, Credit)
    /// </summary>
    public async Task<(bool Success, string Message)> CreateBatchAsync(
        int idJournal,
        DateTime dateSaisie,
        string libelle,
        List<Tuple<int, decimal, decimal>> lignes)
    {
        try
        {
            SetAuthHeader();

            var formatedLignes = new List<object>();
            foreach (var ligne in lignes)
            {
                formatedLignes.Add(new
                {
                    idCompte = ligne.Item1,
                    debit    = ligne.Item2 > 0 ? (decimal?)ligne.Item2 : null,
                    credit   = ligne.Item3 > 0 ? (decimal?)ligne.Item3 : null
                });
            }

            var request = new
            {
                idJournal = idJournal,
                dateSaisie = dateSaisie.ToString("yyyy-MM-ddTHH:mm:ssZ"),
                libelle = libelle,
                lignes = formatedLignes
            };

            var response = await _httpClient.PostAsJsonAsync("ecritures", request);
            var jsonStr = await response.Content.ReadAsStringAsync();

            if (response.IsSuccessStatusCode)
            {
                return (true, "Écriture comptable saisie avec succès.");
            }
            else
            {
                string errorMessage = "Erreur lors de la saisie";
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
            System.Diagnostics.Debug.WriteLine($"EcritureComptableService.CreateBatchAsync: {ex.Message}");
            return (false, "Erreur de communication avec le serveur.");
        }
    }
}
