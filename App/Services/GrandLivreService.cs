using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

public class GrandLivreService
{
    private readonly HttpClient _httpClient;

    public GrandLivreService()
    {
        _httpClient = new HttpClient
        {
            BaseAddress = new Uri("http://localhost:7070/")
        };
		_httpClient.DefaultRequestHeaders.Authorization =
    new System.Net.Http.Headers.AuthenticationHeaderValue("DEBUG");
    }

    public async Task<List<LigneGrandLivre>?> GetGrandLivreAsync(string numeroCompte, string dateDebut, string dateFin)
    {
        try
        {
            var url = $"grand-livre?compte={numeroCompte}&dateDebut={dateDebut}&dateFin={dateFin}";
            return await _httpClient.GetFromJsonAsync<List<LigneGrandLivre>>(url);
        }
        catch
        {
            return null;
        }
    }
}
