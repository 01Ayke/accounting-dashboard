using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class CreateCompteViewModel : INotifyPropertyChanged
{
    private readonly CompteComptableService _compteService = new CompteComptableService();
    private readonly DossierService _dossierService = new DossierService();

    private DossierFactice? _selectedDossier;
    private string _numeroCompte = "";
    private string _libelle = "";
    private string? _message;
    private bool _isSuccess;

    public ObservableCollection<DossierFactice> Dossiers { get; } = new();

    public DossierFactice? SelectedDossier
    {
        get => _selectedDossier;
        set { if (_selectedDossier != value) { _selectedDossier = value; OnPropertyChanged(); } }
    }

    public string NumeroCompte
    {
        get => _numeroCompte;
        set { if (_numeroCompte != value) { _numeroCompte = value; OnPropertyChanged(); } }
    }

    public string Libelle
    {
        get => _libelle;
        set { if (_libelle != value) { _libelle = value; OnPropertyChanged(); } }
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

    public RelayCommand<object> CreateCompteCommand { get; }

    public event PropertyChangedEventHandler? PropertyChanged;

    public CreateCompteViewModel()
    {
        CreateCompteCommand = new RelayCommand<object>(_ => _ = CreateCompteAsync());
        _ = LoadDossiersAsync();
    }

    private async Task LoadDossiersAsync()
    {
        var dossiers = await _dossierService.GetTousLesDossiersAsync();
        if (dossiers != null)
        {
            Dossiers.Clear();
            foreach (var d in dossiers)
            {
                Dossiers.Add(d);
            }
        }
    }

    private async Task CreateCompteAsync()
    {
        Message = null;
        IsSuccess = false;

        if (SelectedDossier == null)
        { Message = "Veuillez sélectionner un dossier."; return; }

        if (string.IsNullOrWhiteSpace(NumeroCompte))
        { Message = "Le numéro de compte est obligatoire."; return; }

        if (string.IsNullOrWhiteSpace(Libelle))
        { Message = "Le libellé est obligatoire."; return; }

        var request = new CreateCompteRequest
        {
            IdDossier    = SelectedDossier.IdDossier,
            NumeroCompte = NumeroCompte,
            Libelle      = Libelle
        };

        var (success, msg) = await _compteService.CreateCompteAsync(request);
        IsSuccess = success;
        Message = msg;

        if (success)
        {
            NumeroCompte = "";
            Libelle = "";
        }
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
