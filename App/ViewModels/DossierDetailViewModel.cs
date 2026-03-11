using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class DossierDetailViewModel : INotifyPropertyChanged
{
    private DossierFactice? _dossier;
    private ObservableCollection<SIGAffichage> _sigAffichage;
    private readonly DossierService _dossierService = new DossierService();

    public DossierFactice? Dossier
    {
        get => _dossier;
        set
        {
            if (_dossier != value)
            {
                _dossier = value;
                OnPropertyChanged();
                System.Diagnostics.Debug.WriteLine($"DossierDetailViewModel: Dossier changé = {value?.NomEntreprise}");
                if (value != null)
                {
                    _ = FetchSIGFromBackendAsync();
                }
            }
        }
    }

    public ObservableCollection<SIGAffichage> SIGAffichage
    {
        get => _sigAffichage;
        set
        {
            if (_sigAffichage != value)
            {
                _sigAffichage = value;
                OnPropertyChanged();
            }
        }
    }

    public event PropertyChangedEventHandler? PropertyChanged;

    public DossierDetailViewModel()
    {
        _sigAffichage = new ObservableCollection<SIGAffichage>();
    }

    private async Task FetchSIGFromBackendAsync()
    {
        if (Dossier == null) return;

        System.Diagnostics.Debug.WriteLine($"FetchSIGFromBackendAsync: Démarrage pour dossier ID={Dossier.IdDossier}");

        var sigBackend = await _dossierService.GetSIGAsync(Dossier.IdDossier);

        if (sigBackend != null)
        {
            System.Diagnostics.Debug.WriteLine("FetchSIGFromBackendAsync: SIG reçus du backend");
            RegenererAffichageSIG(sigBackend);
        }
        else
        {
            System.Diagnostics.Debug.WriteLine("FetchSIGFromBackendAsync: ERREUR - Aucun SIG reçu du backend");
            DisplaySIGError("Erreur : Les données SIG ne sont pas disponibles pour ce dossier.");
        }
    }

    private void RegenererAffichageSIG(SIGBackend sig)
    {
        var affichage = new ObservableCollection<SIGAffichage>
        {
            new SIGAffichage { Label = "Marge Commerciale", Valeur = (decimal)sig.MargeCommerciale },
            new SIGAffichage { Label = "Valeur Ajoutée", Valeur = (decimal)sig.ValeurAjoutee },
            new SIGAffichage { Label = "EBE (Excédent Brut d'Exploitation)", Valeur = (decimal)sig.ExcedentBrut },
            new SIGAffichage { Label = "Résultat d'Exploitation", Valeur = (decimal)sig.ResultatExploitation },
            new SIGAffichage { Label = "RCAI (Résultat Courant Avant Impôt)", Valeur = (decimal)(sig.ResultatExploitation + sig.ResultatFinancier) },
            new SIGAffichage { Label = "Résultat Exceptionnel", Valeur = (decimal)sig.ResultatExceptionnel },
            new SIGAffichage { Label = "Résultat Net", Valeur = (decimal)sig.ResultatExercice }
        };

        SIGAffichage = affichage;
    }

    private void DisplaySIGError(string errorMessage)
    {
        var affichage = new ObservableCollection<SIGAffichage>
        {
            new SIGAffichage
            {
                Label = "Erreur",
                Valeur = 0m,
                Description = errorMessage
            }
        };

        SIGAffichage = affichage;
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}

public class SIGAffichage
{
    public string Label { get; set; } = "";
    public decimal Valeur { get; set; }
    public string Description { get; set; } = "";

    public string ValeurFormatee => Valeur.ToString("C2", System.Globalization.CultureInfo.GetCultureInfo("fr-FR"));

    public string CouleurValeur => Valeur >= 0 ? "Green" : "Red";
}