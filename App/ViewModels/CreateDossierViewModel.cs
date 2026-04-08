using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class CreateDossierViewModel : INotifyPropertyChanged
{
    private readonly DossierService _dossierService = new DossierService();
    private readonly ReferenceDataService _refService = new ReferenceDataService();

    // ─── Propriétés liées aux champs ─────────────────────────────────────────

    private string _denominationSociale = "";
    private string _telephone = "";
    private string _mail = "";
    private string _capital = "";

    private FormeJuridique? _selectedFormeJuridique;
    private RegimeFiscal? _selectedRegimeFiscal;
    private RegimeTva? _selectedRegimeTVA;

    private string _ville = "";
    private string _codePostal = "";
    private string _rue = "";
    private string _pays = "France";

    private string? _message;
    private bool _isSuccess;
    private bool _isLoading;
    private bool _isLoadingDropdowns;

    private string? _emailError;
    private string? _telephoneError;

    // ─── Collections pour les ComboBox ───────────────────────────────────────

    public ObservableCollection<FormeJuridique> FormesJuridiques { get; } = new();
    public ObservableCollection<RegimeFiscal> RegimesFiscaux { get; } = new();
    public ObservableCollection<RegimeTva> RegimesTVA { get; } = new();

    // ─── Bindings ─────────────────────────────────────────────────────────────

    public string DenominationSociale
    {
        get => _denominationSociale;
        set { if (_denominationSociale != value) { _denominationSociale = value; OnPropertyChanged(); } }
    }

    public string Telephone
    {
        get => _telephone;
        set
        {
            if (_telephone != value)
            {
                _telephone = value;
                OnPropertyChanged();
                TelephoneError = (!string.IsNullOrEmpty(value) && !Regex.IsMatch(value, @"^\+33[0-9]{9}$"))
                    ? "Format invalide (ex: +33612345678)"
                    : null;
            }
        }
    }

    public string Mail
    {
        get => _mail;
        set
        {
            if (_mail != value)
            {
                _mail = value;
                OnPropertyChanged();
                EmailError = (!string.IsNullOrEmpty(value) && !value.Contains('@'))
                    ? "Email invalide (@ manquant)"
                    : null;
            }
        }
    }

    public string Capital
    {
        get => _capital;
        set { if (_capital != value) { _capital = value; OnPropertyChanged(); } }
    }

    public FormeJuridique? SelectedFormeJuridique
    {
        get => _selectedFormeJuridique;
        set { if (_selectedFormeJuridique != value) { _selectedFormeJuridique = value; OnPropertyChanged(); } }
    }

    public RegimeFiscal? SelectedRegimeFiscal
    {
        get => _selectedRegimeFiscal;
        set { if (_selectedRegimeFiscal != value) { _selectedRegimeFiscal = value; OnPropertyChanged(); } }
    }

    public RegimeTva? SelectedRegimeTVA
    {
        get => _selectedRegimeTVA;
        set { if (_selectedRegimeTVA != value) { _selectedRegimeTVA = value; OnPropertyChanged(); } }
    }

    public string Ville
    {
        get => _ville;
        set { if (_ville != value) { _ville = value; OnPropertyChanged(); } }
    }

    public string CodePostal
    {
        get => _codePostal;
        set
        {
            if (_codePostal != value)
            {
                _codePostal = value;
                OnPropertyChanged();
                if (value?.Length == 5)
                    _ = LookupCityAsync(value);
            }
        }
    }

    public string Rue
    {
        get => _rue;
        set { if (_rue != value) { _rue = value; OnPropertyChanged(); } }
    }

    public string Pays
    {
        get => _pays;
        set { if (_pays != value) { _pays = value; OnPropertyChanged(); } }
    }

    public string? Message
    {
        get => _message;
        set { if (_message != value) { _message = value; OnPropertyChanged(); } }
    }

    public bool IsSuccess
    {
        get => _isSuccess;
        set { if (_isSuccess != value) { _isSuccess = value; OnPropertyChanged(); } }
    }

    public bool IsLoading
    {
        get => _isLoading;
        set { if (_isLoading != value) { _isLoading = value; OnPropertyChanged(); } }
    }

    public bool IsLoadingDropdowns
    {
        get => _isLoadingDropdowns;
        set { if (_isLoadingDropdowns != value) { _isLoadingDropdowns = value; OnPropertyChanged(); } }
    }

    public string? EmailError
    {
        get => _emailError;
        set { if (_emailError != value) { _emailError = value; OnPropertyChanged(); } }
    }

    public string? TelephoneError
    {
        get => _telephoneError;
        set { if (_telephoneError != value) { _telephoneError = value; OnPropertyChanged(); } }
    }

    // ─── Commandes ────────────────────────────────────────────────────────────

    public RelayCommand<object> CreateDossierCommand { get; }

    public event EventHandler? DossierCree;
    public event PropertyChangedEventHandler? PropertyChanged;

    public CreateDossierViewModel()
    {
        CreateDossierCommand = new RelayCommand<object>(_ => _ = CreateDossierAsync());
        _ = LoadDropdownsAsync();
    }

    // ─── Chargement des dropdowns depuis l'API ────────────────────────────────

    private async Task LoadDropdownsAsync()
    {
        IsLoadingDropdowns = true;
        Message = "Chargement des données...";
        try
        {
            var formesTask = _refService.GetFormesJuridiquesAsync();
            var regimesTask = _refService.GetRegimesFiscauxAsync();
            var tvaTask = _refService.GetRegimesTVAAsync();

            await Task.WhenAll(formesTask, regimesTask, tvaTask);

            foreach (var f in formesTask.Result) FormesJuridiques.Add(f);
            foreach (var r in regimesTask.Result) RegimesFiscaux.Add(r);
            foreach (var t in tvaTask.Result) RegimesTVA.Add(t);

            Message = null;
        }
        catch (Exception ex)
        {
            Message = $"Erreur chargement listes : {ex.Message}";
        }
        finally
        {
            IsLoadingDropdowns = false;
        }
    }

    // ─── Création du dossier ──────────────────────────────────────────────────

    private async Task CreateDossierAsync()
    {
        Message = null;
        IsSuccess = false;

        if (string.IsNullOrWhiteSpace(DenominationSociale))
        { Message = "La dénomination sociale est obligatoire."; return; }

        if (SelectedFormeJuridique == null)
        { Message = "Veuillez sélectionner une forme juridique."; return; }

        if (SelectedRegimeFiscal == null)
        { Message = "Veuillez sélectionner un régime fiscal."; return; }

        if (SelectedRegimeTVA == null)
        { Message = "Veuillez sélectionner un régime de TVA."; return; }

        if (!double.TryParse(Capital.Replace(',', '.'),
            System.Globalization.NumberStyles.Any,
            System.Globalization.CultureInfo.InvariantCulture,
            out double capitalValue))
        { Message = "Le capital doit être un nombre valide."; return; }

        if (!string.IsNullOrEmpty(Mail) && !Mail.Contains('@'))
        { Message = "Email professionnel invalide (@ manquant)."; return; }

        if (!string.IsNullOrEmpty(Telephone) && !Regex.IsMatch(Telephone, @"^\+33[0-9]{9}$"))
        { Message = "Format téléphone invalide (ex: +33612345678)."; return; }

        var request = new CreateDossierRequest
        {
            DenominationSociale = DenominationSociale,
            Telephone           = Telephone,
            Mail                = Mail,
            Capital             = capitalValue,
            FormeJuridique      = new FormeJuridiqueRef { IdFormeJuridique = SelectedFormeJuridique.IdFormeJuridique },
            RegimeFiscal        = new RegimeFiscalRef   { IdRegimeFiscal   = SelectedRegimeFiscal.IdRegimeFiscal   },
            RegimeTVA           = new RegimeTvaRef       { IdRegimeTVA      = SelectedRegimeTVA.IdRegimeTVA         },
            Adresse             = new AdresseRequest
            {
                Pays      = Pays,
                Ville     = Ville,
                Rue       = Rue,
                CodePostal = CodePostal,
                Numero    = "",
                Complement = ""
            }
        };

        IsLoading = true;
        try
        {
            var (success, msg) = await _dossierService.CreateDossierAsync(request);
            IsSuccess = success;
            Message = msg;

            if (success)
            {
                DenominationSociale = "";
                Telephone = "";
                Mail = "";
                Capital = "";
                Ville = "";
                CodePostal = "";
                Rue = "";
                Pays = "France";
                SelectedFormeJuridique = null;
                SelectedRegimeFiscal = null;
                SelectedRegimeTVA = null;
                DossierCree?.Invoke(this, EventArgs.Empty);
            }
        }
        finally
        {
            IsLoading = false;
        }
    }

    private async Task LookupCityAsync(string cp)
    {
        var cities = await _refService.GetCityByPostalCodeAsync(cp);
        if (cities.Count > 0)
            Ville = cities[0];
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
