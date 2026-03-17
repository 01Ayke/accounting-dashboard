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

    private string _numeroCompte = "";
    private string _libelle = "";
    private string _racinePcg = "";
    private string? _message;

    public string NumeroCompte
    {
        get => _numeroCompte;
        set
        {
            if (_numeroCompte != value)
            {
                _numeroCompte = value;
                OnPropertyChanged();
            }
        }
    }

    public string Libelle
    {
        get => _libelle;
        set
        {
            if (_libelle != value)
            {
                _libelle = value;
                OnPropertyChanged();
            }
        }
    }

    public string RacinePcg
    {
        get => _racinePcg;
        set
        {
            if (_racinePcg != value)
            {
                _racinePcg = value;
                OnPropertyChanged();
            }
        }
    }

    public string? Message
    {
        get => _message;
        set
        {
            if (_message != value)
            {
                _message = value;
                OnPropertyChanged();
            }
        }
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

        if (string.IsNullOrWhiteSpace(NumeroCompte) ||
            string.IsNullOrWhiteSpace(Libelle) ||
            string.IsNullOrWhiteSpace(RacinePcg))
        {
            Message = "Veuillez remplir tous les champs.";
            return;
        }

        var request = new CreateCompteRequest
        {
            NumeroCompte = NumeroCompte,
            Libelle = Libelle,
            RacinePcg = RacinePcg
        };

        var success = await _compteService.CreateCompteAsync(request);

        Message = success
            ? "Compte comptable créé avec succès."
            : "Erreur lors de la création du compte.";
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}