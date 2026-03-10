using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using App.Models;
using System.Collections.Generic;
using System.Text.Json;

namespace App.Services;

public class DossierService
{
    private readonly HttpClient _httpClient;

    public DossierService()
    {
        _httpClient = new HttpClient
        {
            BaseAddress = new Uri("http://localhost:7070/")
        };
    }

    public async Task<DossierFactice?> GetDossierAsync(int dossierId)
    {
        try
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.GetDossierAsync: Appel /file/{dossierId}");
            
            var response = await _httpClient.GetAsync($"file/{dossierId}");
            if (response.IsSuccessStatusCode)
            {
                var json = await response.Content.ReadAsStringAsync();
                System.Diagnostics.Debug.WriteLine($"DossierService: Response reçue");
                
                // Parser le JSON
                using (JsonDocument doc = JsonDocument.Parse(json))
                {
                    var root = doc.RootElement;
                    var fileObj = root.GetProperty("file");
                    
                    // Récupérer le premier représentant (principal)
                    string prenom = "";
                    string nom = "";
                    string emailRepresentant = "";
                    
                    if (fileObj.TryGetProperty("representants", out var representants) && representants.GetArrayLength() > 0)
                    {
                        var firstRep = representants[0];
                        prenom = firstRep.GetProperty("prenom").GetString() ?? "";
                        nom = firstRep.GetProperty("nom").GetString() ?? "";
                        emailRepresentant = firstRep.GetProperty("mail").GetString() ?? "";
                    }

                    return new DossierFactice
                    {
                        IdDossier = dossierId,
                        NomEntreprise = fileObj.GetProperty("denominationSociale").GetString() ?? "",
                        Siret = fileObj.GetProperty("id").GetInt32().ToString(),
                        Prenom = prenom,
                        Nom = nom,
                        EmailContact = emailRepresentant,
                        Secteur = "Comptabilité",
                        Statut = "Actif"
                    };
                }
            }
            System.Diagnostics.Debug.WriteLine($"DossierService.GetDossierAsync: Erreur {response.StatusCode}");
            return null;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.GetDossierAsync: Exception = {ex.Message}");
            return null;
        }
    }

    public async Task<List<DossierFactice>?> GetAllDossiersAsync(int[] dossiersIds)
    {
        var dossiers = new List<DossierFactice>();
        
        foreach (var id in dossiersIds)
        {
            var dossier = await GetDossierAsync(id);
            if (dossier != null)
            {
                dossiers.Add(dossier);
            }
        }
        
        return dossiers.Count > 0 ? dossiers : null;
    }

    public async Task<bool> CreateDossierAsync(CreateDossierRequest request)
    {
        try
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.CreateDossierAsync: Création d'un nouveau dossier");
            
            var response = await _httpClient.PostAsJsonAsync("file", request);
            
            if (response.IsSuccessStatusCode)
            {
                System.Diagnostics.Debug.WriteLine("DossierService.CreateDossierAsync: Dossier créé avec succès");
                return true;
            }
            
            var errorContent = await response.Content.ReadAsStringAsync();
            System.Diagnostics.Debug.WriteLine($"DossierService.CreateDossierAsync: Erreur {response.StatusCode} - {errorContent}");
            return false;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.CreateDossierAsync: Exception = {ex.Message}");
            return false;
        }
    }

    public async Task<SIGBackend?> GetSIGAsync(int dossierId)
    {
        try
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.GetSIGAsync: Appel /file/{dossierId}");
            
            var response = await _httpClient.GetAsync($"file/{dossierId}");
            if (response.IsSuccessStatusCode)
            {
                var json = await response.Content.ReadAsStringAsync();
                System.Diagnostics.Debug.WriteLine($"DossierService.GetSIGAsync: Contenu brut reçu: {json}");
                System.Diagnostics.Debug.WriteLine($"DossierService.GetSIGAsync: Response reçue");
                
                using (JsonDocument doc = JsonDocument.Parse(json))
                {
                    var root = doc.RootElement;
                    System.Diagnostics.Debug.WriteLine($"DossierService.GetSIGAsync: Root properties: {string.Join(", ", root.EnumerateObject().Select(p => p.Name))}");
                    
                    // Récupérer l'objet SIG du backend
                    if (root.TryGetProperty("sig", out var sigObj))
                    {
                        System.Diagnostics.Debug.WriteLine($"DossierService.GetSIGAsync: SIG trouvé");
                        
                        // Vérifier quels SIG sont présents
                        var sigProperties = new HashSet<string>();
                        foreach (var prop in sigObj.EnumerateObject())
                        {
                            sigProperties.Add(prop.Name);
                        }
                        System.Diagnostics.Debug.WriteLine($"DossierService.GetSIGAsync: SIG disponibles: {string.Join(", ", sigProperties)}");
                        
                        var sig = new SIGBackend
                        {
                            MargeCommerciale = sigObj.TryGetProperty("margeCommerciale", out var mc) ? mc.GetDouble() : double.NaN,
                            ProductionExercice = sigObj.TryGetProperty("productionExercice", out var pe) ? pe.GetDouble() : double.NaN,
                            ValeurAjoutee = sigObj.TryGetProperty("valeurAjoutee", out var va) ? va.GetDouble() : double.NaN,
                            ExcedentBrut = sigObj.TryGetProperty("excedentBrut", out var eb) ? eb.GetDouble() : double.NaN,
                            ResultatExploitation = sigObj.TryGetProperty("resultatExploitation", out var re) ? re.GetDouble() : double.NaN,
                            ResultatFinancier = sigObj.TryGetProperty("resultatFinancier", out var rf) ? rf.GetDouble() : double.NaN,
                            ResultatExceptionnel = sigObj.TryGetProperty("resultatExceptionnel", out var rex) ? rex.GetDouble() : double.NaN,
                            ResultatExercice = sigObj.TryGetProperty("resultatExercice", out var rexe) ? rexe.GetDouble() : double.NaN
                        };
                        
                        System.Diagnostics.Debug.WriteLine($"  Marge commerciale: {sig.MargeCommerciale}");
                        System.Diagnostics.Debug.WriteLine($"  Production exercice: {sig.ProductionExercice}");
                        System.Diagnostics.Debug.WriteLine($"  Valeur ajoutée: {sig.ValeurAjoutee}");
                        System.Diagnostics.Debug.WriteLine($"  EBE: {sig.ExcedentBrut}");
                        System.Diagnostics.Debug.WriteLine($"  Resultat exploitation: {sig.ResultatExploitation}");
                        System.Diagnostics.Debug.WriteLine($"  Resultat financier: {sig.ResultatFinancier}");
                        System.Diagnostics.Debug.WriteLine($"  Resultat exceptionnel: {sig.ResultatExceptionnel}");
                        System.Diagnostics.Debug.WriteLine($"  Resultat exercice: {sig.ResultatExercice}");
                        
                        return sig;
                    }
                    System.Diagnostics.Debug.WriteLine("DossierService.GetSIGAsync: SIG NOT FOUND dans la réponse");
                }
                return null;
            }
            System.Diagnostics.Debug.WriteLine($"DossierService.GetSIGAsync: Erreur {response.StatusCode}");
            return null;
        }
        catch (Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"DossierService.GetSIGAsync: Exception = {ex.Message}");
            return null;
        }
    }
}
