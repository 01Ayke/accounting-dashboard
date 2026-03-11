using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class AssignDossierViewModel : INotifyPropertyChanged
{
    private readonly DossierService _dossierService = new DossierService();

    private string _dossierId = "";
    private string _userId = "";
    private string? _message;

    public string DossierId
    {
        get => _dossierId;
        set
        {
            if (_dossierId != value)
            {
                _dossierId = value;
                OnPropertyChanged();
            }
        }
    }

    public string UserId
    {
        get => _userId;
        set
        {
            if (_userId != value)
            {
                _userId = value;
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

    public RelayCommand<object> AssignCommand { get; }

    public event PropertyChangedEventHandler? PropertyChanged;

    public AssignDossierViewModel()
    {
        AssignCommand = new RelayCommand<object>(_ => _ = AssignAsync());
    }

    private async Task AssignAsync()
    {
        Message = null;

        if (!int.TryParse(DossierId, out var dossierId))
        {
            Message = "ID dossier invalide.";
            return;
        }

        if (!int.TryParse(UserId, out var userId))
        {
            Message = "ID utilisateur invalide.";
            return;
        }

        var request = new AssignDossierRequest
        {
            UserId = userId
        };

        var success = await _dossierService.AssignDossierAsync(dossierId, request);

        Message = success
            ? "Dossier attribué avec succès."
            : "Erreur lors de l'attribution du dossier.";
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}