using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class NouveauTiersViewModel : INotifyPropertyChanged
{
    private readonly CompteComptableService _service = new();

    private List<CompteDto> _comptesBase       = new();
    private List<CompteDto> _comptesAgregation = new();

    public ObservableCollection<string> Types { get; } = new()
    {
        "40 - Fournisseurs",
        "41 - Clients"
    };

    private string? _selectedType;
    public string? SelectedType
    {
        get => _selectedType;
        set
        {
            _selectedType = value;
            OnPropertyChanged();
            RefreshRattachement();
            RefreshPreview();
        }
    }

    private bool _isAgregation = true;
    public bool IsAgregation
    {
        get => _isAgregation;
        set
        {
            _isAgregation = value;
            OnPropertyChanged();
            OnPropertyChanged(nameof(CodeLabel));
            OnPropertyChanged(nameof(CodeHint));
            OnPropertyChanged(nameof(RattachementLabel));
            RefreshRattachement();
            RefreshPreview();
        }
    }

    public ObservableCollection<CompteDto> RattachementItems { get; } = new();

    private CompteDto? _selectedRattachement;
    public CompteDto? SelectedRattachement
    {
        get => _selectedRattachement;
        set { _selectedRattachement = value; OnPropertyChanged(); RefreshPreview(); }
    }

    private string _code = string.Empty;
    public string Code
    {
        get => _code;
        set { _code = value; OnPropertyChanged(); RefreshPreview(); }
    }

    private string _libelle = string.Empty;
    public string Libelle
    {
        get => _libelle;
        set { _libelle = value; OnPropertyChanged(); }
    }

    private string _preview = "—";
    public string NumeroPreview
    {
        get => _preview;
        private set { _preview = value; OnPropertyChanged(); }
    }

    public string CodeLabel         => IsAgregation ? "Code d'agrégation (2 chiffres) *" : "N° de série (3 chiffres) *";
    public string CodeHint          => IsAgregation ? "ex : 01, 12, 99" : "ex : 001, 042, 999";
    public string RattachementLabel => IsAgregation
        ? "Compte de rattachement — base PCG (401, 411…) *"
        : "Compte de rattachement — agrégation (40101, 41101…) *";

    private string? _errorAux;
    public string? ErrorAux
    {
        get => _errorAux;
        set { _errorAux = value; OnPropertyChanged(); }
    }

    private string? _successAux;
    public string? SuccessAux
    {
        get => _successAux;
        set { _successAux = value; OnPropertyChanged(); }
    }

    public RelayCommand<object> CreerCommand { get; }

    public NouveauTiersViewModel()
    {
        CreerCommand = new RelayCommand<object>(_ => _ = CreerAuxiliaireAsync());
    }

    public async Task InitAsync()
    {
        var dossierId = SessionManager.CurrentDossierId;
        if (!dossierId.HasValue)
        {
            ErrorAux = "Aucun dossier sélectionné.";
            return;
        }

        try
        {
            var bases    = await _service.GetAllComptesAgregationAsync(dossierId.Value);
            var agregats = await _service.GetAllCompteAuxiliairesAsync(dossierId.Value);

            _comptesBase       = bases.ToList();
            _comptesAgregation = agregats.ToList();

            // Reset pour forcer RefreshRattachement même si SelectedType était déjà positionné
            SelectedType = null;
            SelectedType = Types.First();
        }
        catch (Exception ex)
        {
            ErrorAux = $"Erreur chargement : {ex.Message}";
        }
    }

    private void RefreshRattachement()
    {
        RattachementItems.Clear();
        SelectedRattachement = null;
        if (string.IsNullOrEmpty(SelectedType)) return;

        var prefix = SelectedType.StartsWith("40") ? "40" : "41";

        var source = IsAgregation
            ? _comptesBase.Where(c => c.NumeroCompte.StartsWith(prefix))
            : _comptesAgregation.Where(c => c.NumeroCompte.StartsWith(prefix));

        foreach (var item in source)
            RattachementItems.Add(item);

        if (RattachementItems.Any())
            SelectedRattachement = RattachementItems.First();
    }

    private void RefreshPreview()
    {
        if (SelectedRattachement == null || string.IsNullOrWhiteSpace(Code))
        { NumeroPreview = "—"; return; }

        var nr = SelectedRattachement.NumeroCompte;
        NumeroPreview = IsAgregation
            ? (Code.Length == 2 ? $"{nr}{Code}" : "—")   // 401 + 01 = 40101
            : (Code.Length == 3 ? $"{nr}{Code}" : "—");  // 40101 + 001 = 40101001

    }

    private async Task CreerAuxiliaireAsync()
    {
        ErrorAux   = null;
        SuccessAux = null;

        if (SelectedRattachement == null)
        { ErrorAux = "Sélectionnez un compte de rattachement."; return; }

        if (string.IsNullOrEmpty(Code) || !System.Text.RegularExpressions.Regex.IsMatch(Code, "^[0-9]+$"))
        { ErrorAux = "Le code ne doit contenir que des chiffres."; return; }

        if (IsAgregation && Code.Length != 2)
        { ErrorAux = "Le code d'agrégation doit être exactement 2 chiffres (ex: 01)."; return; }

        if (!IsAgregation && Code.Length != 3)
        { ErrorAux = "Le N° de série doit être exactement 3 chiffres (ex: 001)."; return; }

        if (string.IsNullOrWhiteSpace(Libelle))
        { ErrorAux = "Le libellé est obligatoire."; return; }

        var rattachNr  = SelectedRattachement.NumeroCompte;
        // Agrégation : 401 + 01 = 40101 (5 chiffres, retourné par /dossier-comptes-rattachement)
        // Tiers      : 40101 + 001 = 40101001 (8 chiffres)
        var fullNumero = IsAgregation ? $"{rattachNr}{Code}" : $"{rattachNr}{Code}";

        int idCompteRattachement;
        if (IsAgregation)
        {
            idCompteRattachement = SelectedRattachement.IdCompteApi ?? 0;
        }
        else
        {
            var rootNr = rattachNr.Length >= 3 ? rattachNr[..3] : rattachNr;
            var root   = _comptesBase.FirstOrDefault(c => c.NumeroCompte == rootNr);
            if (root?.IdCompteApi == null)
            { ErrorAux = $"Compte de base '{rootNr}' introuvable."; return; }
            idCompteRattachement = root.IdCompteApi.Value;
        }

        var req = new CreateCompteAuxiliaireRequest
        {
            IdTypeCompteAuxiliaire = 1,
            IdCompteRattachement   = idCompteRattachement,
            Numero                 = fullNumero,
            Libelle                = Libelle
        };

        var (ok, msg) = await _service.CreateCompteAuxiliaireAsync(req);
        if (ok)
        {
            SuccessAux = $"✅  {fullNumero} — {Libelle} créé.";
            Code       = string.Empty;
            Libelle    = string.Empty;
            RefreshPreview();
            await InitAsync();
        }
        else
        {
            ErrorAux = msg;
        }
    }

    public event PropertyChangedEventHandler? PropertyChanged;
    protected void OnPropertyChanged([CallerMemberName] string? name = null) =>
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
