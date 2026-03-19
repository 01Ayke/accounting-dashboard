using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class CreateDossierViewModel : INotifyPropertyChanged
{
    private readonly DossierService _dossierService = new DossierService();

    private string _denominationSociale = "";
    private string _siret = "";
    private string _formeJuridique = "";
    private string _regimeFiscal = "";
    private string _prenom = "";
    private string _nom = "";
    private string _mail = "";
    private string? _message;
    private bool _isSuccess;

    public string DenominationSociale
    {
        get => _denominationSociale;
        set
        {
            if (_denominationSociale != value)
            {
                _denominationSociale = value;
                OnPropertyChanged();
            }
        }
    }

    public string Siret
    {
        get => _siret;
        set
        {
            if (_siret != value)
            {
                _siret = value;
                OnPropertyChanged();
            }
        }
    }

    public string FormeJuridique
    {
        get => _formeJuridique;
        set
        {
            if (_formeJuridique != value)
            {
                _formeJuridique = value;
                OnPropertyChanged();
            }
        }
    }

    public string RegimeFiscal
    {
        get => _regimeFiscal;
        set
        {
            if (_regimeFiscal != value)
            {
                _regimeFiscal = value;
                OnPropertyChanged();
            }
        }
    }

    public string Prenom
    {
        get => _prenom;
        set
        {
            if (_prenom != value)
            {
                _prenom = value;
                OnPropertyChanged();
            }
        }
    }

    public string Nom
    {
        get => _nom;
        set
        {
            if (_nom != value)
            {
                _nom = value;
                OnPropertyChanged();
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

    public bool IsSuccess
    {
        get => _isSuccess;
        set
        {
            if (_isSuccess != value)
            {
                _isSuccess = value;
                OnPropertyChanged();
            }
        }
    }

    public RelayCommand<object> CreateDossierCommand { get; }

    public event PropertyChangedEventHandler? PropertyChanged;

    public CreateDossierViewModel()
    {
        CreateDossierCommand = new RelayCommand<object>(_ => _ = CreateDossierAsync());
    }

    private async Task CreateDossierAsync()
    {
        Message = null;
        IsSuccess = false;

        if (string.IsNullOrWhiteSpace(DenominationSociale) ||
            //string.IsNullOrWhiteSpace(Siret) ||
            string.IsNullOrWhiteSpace(FormeJuridique) ||
            string.IsNullOrWhiteSpace(RegimeFiscal) ||
            string.IsNullOrWhiteSpace(Prenom) ||
            string.IsNullOrWhiteSpace(Nom) ||
            string.IsNullOrWhiteSpace(Mail))
        {
            Message = "Veuillez remplir tous les champs.";
            return;
        }

        var request = new CreateDossierRequest
        {
            DenominationSociale = DenominationSociale,
            //Siret = Siret,
            FormeJuridique = FormeJuridique,
            RegimeFiscal = RegimeFiscal,
            Prenom = Prenom,
            Nom = Nom,
            Mail = Mail
        };

        var success = await _dossierService.CreateDossierAsync(request);

        if (success)
        {
            IsSuccess = true;
            Message = "Dossier créé avec succès.";

            DenominationSociale = "";
            //Siret = "";
            FormeJuridique = "";
            RegimeFiscal = "";
            Prenom = "";
            Nom = "";
            Mail = "";
        }
        else
        {
            IsSuccess = false;
            Message = "Erreur lors de la création du dossier.";
        }
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}