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

    private string _idDossier = "";
    private string _numeroCompte = "";
    private string _libelle = "";
    private string? _message;
    private bool _isSuccess;

    public string IdDossier
    {
        get => _idDossier;
        set { if (_idDossier != value) { _idDossier = value; OnPropertyChanged(); } }
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
    }

    private async Task CreateCompteAsync()
    {
        Message = null;
        IsSuccess = false;

        if (!int.TryParse(IdDossier, out int idDossier))
        { Message = "ID dossier invalide (entier requis)."; return; }

        if (string.IsNullOrWhiteSpace(NumeroCompte))
        { Message = "Le numéro de compte est obligatoire."; return; }

        if (string.IsNullOrWhiteSpace(Libelle))
        { Message = "Le libellé est obligatoire."; return; }

        var request = new CreateCompteRequest
        {
            IdDossier = idDossier,
            Numero    = NumeroCompte,
            Libelle   = Libelle
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
