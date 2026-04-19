using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Services;

namespace App.ViewModels;

using App.Models;

public class SaisieLigneViewModel : INotifyPropertyChanged
{
    private string _compte = string.Empty;
    public string Compte
    {
        get => _compte;
        set { if (_compte != value) { _compte = value; OnPropertyChanged(); } }
    }

    private decimal? _debit;
    public decimal? Debit
    {
        get => _debit;
        set { if (_debit != value) { _debit = value; OnPropertyChanged(); } }
    }

    private decimal? _credit;
    public decimal? Credit
    {
        get => _credit;
        set { if (_credit != value) { _credit = value; OnPropertyChanged(); } }
    }

    // Utilisé seulement pour l'API (idCompte) mais on peut le mocker
    public int IdCompte { get; set; } = 1; 

    public event PropertyChangedEventHandler? PropertyChanged;
    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}

public class SaisieEcritureViewModel : INotifyPropertyChanged
{
    private string _dateSaisie = System.DateTime.Now.ToString("dd/MM/yyyy");
    private string _libelleEcriture = "";

    public ObservableCollection<CompteDto>  ComptesDisponibles  { get; } = new();
    public ObservableCollection<JournalDto> JournauxDisponibles { get; } = new();

    private JournalDto? _selectedJournal;
    private CompteDto?  _selectedCompte;
    private decimal?    _nouveauDebit;
    private decimal?    _nouveauCredit;
    private string?     _errorMessage;
    private string?     _successMessage;

    public JournalDto? SelectedJournal
    {
        get => _selectedJournal;
        set { if (_selectedJournal != value) { _selectedJournal = value; OnPropertyChanged(); } }
    }

    public string DateSaisie
    {
        get => _dateSaisie;
        set { if (_dateSaisie != value) { _dateSaisie = value; OnPropertyChanged(); } }
    }

    public string LibelleEcriture
    {
        get => _libelleEcriture;
        set { if (_libelleEcriture != value) { _libelleEcriture = value; OnPropertyChanged(); } }
    }

    public CompteDto? SelectedCompte
    {
        get => _selectedCompte;
        set { if (_selectedCompte != value) { _selectedCompte = value; OnPropertyChanged(); } }
    }

    public decimal? NouveauDebit
    {
        get => _nouveauDebit;
        set { if (_nouveauDebit != value) { _nouveauDebit = value; OnPropertyChanged(); } }
    }

    public decimal? NouveauCredit
    {
        get => _nouveauCredit;
        set { if (_nouveauCredit != value) { _nouveauCredit = value; OnPropertyChanged(); } }
    }

    public string? ErrorMessage
    {
        get => _errorMessage;
        set { if (_errorMessage != value) { _errorMessage = value; OnPropertyChanged(); } }
    }

    public string? SuccessMessage
    {
        get => _successMessage;
        set { if (_successMessage != value) { _successMessage = value; OnPropertyChanged(); } }
    }

    public decimal TotalDebit => Lignes.Sum(l => l.Debit ?? 0);
    public decimal TotalCredit => Lignes.Sum(l => l.Credit ?? 0);
    public decimal Equilibre => TotalDebit - TotalCredit;

    public ObservableCollection<SaisieLigneViewModel> Lignes { get; } = new();

    public RelayCommand<object> AjouterLigneCommand { get; }
    public RelayCommand<object> ValiderEcritureCommand { get; }

    public SaisieEcritureViewModel()
    {
        AjouterLigneCommand = new RelayCommand<object>(_ => AjouterLigne());
        ValiderEcritureCommand = new RelayCommand<object>(_ => _ = ValiderEcritureAsync());
        // NE PAS appeler ChargerComptesAsync() ici — CurrentDossierId n'est pas encore disponible
        // dans le constructeur (le DataContext est instancié via XAML).
        // Appeler InitAsync() depuis le code-behind (événement Loaded).
    }

    /// <summary>
    /// À appeler depuis SaisieEcritureView.xaml.cs dans l'événement Loaded,
    /// quand la session est garantie d'être initialisée.
    /// </summary>
    public async Task InitAsync()
    {
        await ChargerComptesAsync();
        await ChargerJournauxAsync();
    }

    private async Task ChargerComptesAsync()
    {
        var dossierId = SessionManager.CurrentDossierId;
        if (!dossierId.HasValue) return;

        try
        {
            var service = new App.Services.CompteComptableService();

            var comptes    = await service.GetAllComptesAsync(dossierId.Value);
            var agregation = await service.GetAllCompteAuxiliairesAsync(dossierId.Value);
            var tiers      = await service.GetAllComptesTiersAsync(dossierId.Value);

            var sorted = comptes
                .Concat(agregation)
                .Concat(tiers)
                .OrderBy(c => long.TryParse(c.NumeroCompte, out var n) ? n : long.MaxValue);

            ComptesDisponibles.Clear();
            foreach (var item in sorted) ComptesDisponibles.Add(item);
        }
        catch (System.Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"Erreur chargement comptes: {ex.Message}");
        }
    }

    private async Task ChargerJournauxAsync()
    {
        var dossierId = SessionManager.CurrentDossierId;
        if (!dossierId.HasValue) return;
        try
        {
            var journaux = await new JournalService().GetJournauxAsync(dossierId.Value);
            JournauxDisponibles.Clear();
            foreach (var j in journaux) JournauxDisponibles.Add(j);
            if (JournauxDisponibles.Count > 0) SelectedJournal = JournauxDisponibles[0];
        }
        catch (System.Exception ex)
        {
            System.Diagnostics.Debug.WriteLine($"Erreur chargement journaux: {ex.Message}");
        }
    }

    private void AjouterLigne()
    {
        ErrorMessage = null;
        SuccessMessage = null;

        if (SelectedCompte == null)
        {
            ErrorMessage = "Vous devez sélectionner un compte.";
            return;
        }

        if ((NouveauDebit == null || NouveauDebit <= 0) && (NouveauCredit == null || NouveauCredit <= 0))
        {
            ErrorMessage = "Vous devez saisir un montant au débit OU au crédit.";
            return;
        }

        if (NouveauDebit > 0 && NouveauCredit > 0)
        {
            ErrorMessage = "Une ligne ne peut pas avoir à la fois un débit et un crédit.";
            return;
        }

        Lignes.Add(new SaisieLigneViewModel
        {
            Compte = SelectedCompte.DisplayText,
            Debit = NouveauDebit > 0 ? NouveauDebit : null,
            Credit = NouveauCredit > 0 ? NouveauCredit : null,
            IdCompte = SelectedCompte.IdCompte
        });

        // Calcul des totaux pour mise à jour UI
        OnPropertyChanged(nameof(TotalDebit));
        OnPropertyChanged(nameof(TotalCredit));
        OnPropertyChanged(nameof(Equilibre));

        // Remise à zéro des champs de saisie
        SelectedCompte = null;
        NouveauDebit = null;
        NouveauCredit = null;
    }

    private async Task ValiderEcritureAsync()
    {
        ErrorMessage = null;
        SuccessMessage = null;

        if (SelectedJournal == null)
        {
            ErrorMessage = "Vous devez sélectionner un journal.";
            return;
        }

        if (Lignes.Count < 2)
        {
            ErrorMessage = "Une écriture doit comporter au moins deux lignes.";
            return;
        }

        if (Equilibre != 0)
        {
            ErrorMessage = $"L'écriture n'est pas équilibrée (Différence : {Equilibre}). Le débit et le crédit doivent être égaux.";
            return;
        }

        if (!SessionManager.CurrentDossierId.HasValue)
        {
            ErrorMessage = "Erreur : Impossible d'identifier le dossier en cours.";
            return;
        }

        var ecritureService = new EcritureComptableService();
        var (success, msg) = await ecritureService.CreateBatchAsync(
            SelectedJournal.Id,
            System.DateTime.Now,
            LibelleEcriture,
            Lignes.Select(l => new Tuple<int, decimal, decimal>(l.IdCompte, l.Debit ?? 0, l.Credit ?? 0)).ToList()
        );

        if (success)
        {
            SuccessMessage = msg;
            Lignes.Clear();
            OnPropertyChanged(nameof(TotalDebit));
            OnPropertyChanged(nameof(TotalCredit));
            OnPropertyChanged(nameof(Equilibre));
            LibelleEcriture = "";
        }
        else
        {
            ErrorMessage = msg;
        }
    }

    public event PropertyChangedEventHandler? PropertyChanged;
    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
