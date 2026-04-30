using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text.Json;
using System.Threading.Tasks;
using App.Models;

namespace App.Services;

/// <summary>
/// Gère l'accès au Grand Livre.
///
/// Endpoint : GET /grandlivre  (authentification requise)
/// Corps JSON : { "idDossier": int, "debut": "YYYY-MM-DD", "fin": "YYYY-MM-DD" }
/// Réponse    : { "status": "success",
///               "message": { "denominationSociale": "...",
///                            "bilanCompte": { "401": [...], "411": [...] } } }
///
/// NOTE : Le backend utilise GET avec un corps JSON (non standard mais valide).
/// On utilise HttpRequestMessage pour contourner la limitation de HttpClient.
/// </summary>
public class GrandLivreService
{
    private readonly HttpClient _httpClient;

    public GrandLivreService()
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
    /// Charge les lignes du grand livre pour un dossier et une période.
    /// dateDebut / dateFin : format "YYYY-MM-DD"
    /// </summary>
    public async Task<(List<LigneGrandLivre> Lignes, string DenominationSociale, string Error)>
        GetGrandLivreAsync(int idDossier, string dateDebut, string dateFin)
    {
        try
        {
            SetAuthHeader();

            // Jackson (Java) déserialise java.util.Date depuis des epoch ms (long)
            // On convertit "YYYY-MM-DD" en millisecondes depuis l'epoch UTC
            long debutMs = new DateTimeOffset(DateTime.Parse(dateDebut), TimeSpan.Zero).ToUnixTimeMilliseconds();
            long finMs   = new DateTimeOffset(DateTime.Parse(dateFin),   TimeSpan.Zero).ToUnixTimeMilliseconds();

            var requestBody = new
            {
                idDossier,
                debut = debutMs,
                fin   = finMs
            };

            // GET avec corps JSON (non standard — requis par le backend)
            var request = new HttpRequestMessage(HttpMethod.Get, "grandlivre")
            {
                Content = JsonContent.Create(requestBody)
            };

            // Propager l'en-tête Authorization sur le message aussi
            var token = SessionManager.Token;
            if (!string.IsNullOrEmpty(token))
                request.Headers.Authorization =
                    new System.Net.Http.Headers.AuthenticationHeaderValue(token);

            var response = await _httpClient.SendAsync(request);
            var body = await response.Content.ReadAsStringAsync();
            System.Diagnostics.Debug.WriteLine($"GrandLivreService: {response.StatusCode} — {body[..Math.Min(300, body.Length)]}");

            if (!response.IsSuccessStatusCode)
            {
                string errMsg = $"Erreur {response.StatusCode}";
                try
                {
                    using var errDoc = JsonDocument.Parse(body);
                    errMsg = errDoc.RootElement.TryGetProperty("message", out var em)
                        ? em.GetString() ?? errMsg : errMsg;
                }
                catch { }
                return (new List<LigneGrandLivre>(), "", errMsg);
            }

            using var doc = JsonDocument.Parse(body);
            var message = doc.RootElement.GetProperty("message");

            var denominationSociale = message.TryGetProperty("denominationSociale", out var ds)
                ? ds.GetString() ?? "" : "";

            var lignes = new List<LigneGrandLivre>();

            if (!message.TryGetProperty("bilanCompte", out var bilanCompte))
                return (lignes, denominationSociale, "Aucune donnée.");

            // bilanCompte est un objet { "401": [...], "411": [...] }
            foreach (var compteEntry in bilanCompte.EnumerateObject())
            {
                string numeroCompte = compteEntry.Name;
                decimal solde = 0m;

                foreach (var ecriture in compteEntry.Value.EnumerateArray())
                {
                    decimal debit  = ecriture.TryGetProperty("debit",  out var d) && d.ValueKind != JsonValueKind.Null ? (decimal)d.GetDouble() : 0m;
                    decimal credit = ecriture.TryGetProperty("credit", out var c) && c.ValueKind != JsonValueKind.Null ? (decimal)c.GetDouble() : 0m;
                    solde += debit - credit;

                    // Date — peut être un objet Java LocalDate sérialisé en tableau ou en string
                    string dateStr = "";
                    if (ecriture.TryGetProperty("date", out var dateEl))
                    {
                        if (dateEl.ValueKind == JsonValueKind.String)
                            dateStr = dateEl.GetString() ?? "";
                        else if (dateEl.ValueKind == JsonValueKind.Number)
                        {
                            // Jackson sérialise java.util.Date en epoch ms par défaut
                            dateStr = DateTimeOffset.FromUnixTimeMilliseconds(dateEl.GetInt64())
                                                    .ToString("yyyy-MM-dd");
                        }
                        else if (dateEl.ValueKind == JsonValueKind.Array)
                        {
                            // Format Java LocalDate : [year, month, day]
                            var arr = dateEl.EnumerateArray().ToList();
                            if (arr.Count >= 3)
                                dateStr = $"{arr[0].GetInt32():0000}-{arr[1].GetInt32():00}-{arr[2].GetInt32():00}";
                        }
                    }

                    string libelle        = ecriture.TryGetProperty("libelle",        out var lib) ? lib.GetString() ?? "" : "";
                    string codeJournal    = ecriture.TryGetProperty("codeJournal",    out var cj)  ? cj.GetString()  ?? "" : "";
                    string libelleJournal = ecriture.TryGetProperty("libelleJournal", out var lj)  ? lj.GetString()  ?? "" : "";

                    lignes.Add(new LigneGrandLivre
                    {
                        NumeroCompte    = numeroCompte,
                        Date            = dateStr,
                        CodeJournal     = codeJournal,
                        LibelleJournal  = libelleJournal,
                        Libelle         = libelle,
                        Debit           = debit,
                        Credit          = credit,
                        SoldeProgressif = solde
                    });
                }
            }

            return (lignes, denominationSociale, "");
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"GrandLivreService.GetGrandLivreAsync: Exception = {ex.Message}");
            return (new List<LigneGrandLivre>(), "", $"Erreur : {ex.Message}");
        }
    }
}
