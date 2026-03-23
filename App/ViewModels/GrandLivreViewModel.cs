using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class GrandLivreViewModel : INotifyPropertyChanged
{
    private readonly GrandLivreService _grandLivreService = new GrandLivreService();

    private string _idDossier = "";
    private string _dateDebut = "";
    private string _dateFin = "";
    private string? _message;
    private string _denominationSociale = "";
    private bool _isLoading;

    public string IdDossier
    {
        get => _idDossier;
        set { if (_idDossier != value) { _idDossier = value; OnPropertyChanged(); } }
    }

    public string DateDebut
    {
        get => _dateDebut;
        set { if (_dateDebut != value) { _dateDebut = value; OnPropertyChanged(); } }
    }

    public string DateFin
    {
        get => _dateFin;
        set { if (_dateFin != value) { _dateFin = value; OnPropertyChanged(); } }
    }

    public string? Message
    {
        get => _message;
        set { if (_message != value) { _message = value; OnPropertyChanged(); } }
    }

    public string DenominationSociale
    {
        get => _denominationSociale;
        set { if (_denominationSociale != value) { _denominationSociale = value; OnPropertyChanged(); } }
    }

    public bool IsLoading
    {
        get => _isLoading;
        set { if (_isLoading != value) { _isLoading = value; OnPropertyChanged(); } }
    }

    public ObservableCollection<LigneGrandLivre> Lignes { get; } = new();

    public RelayCommand<object> LoadGrandLivreCommand { get; }

    public event PropertyChangedEventHandler? PropertyChanged;

    public GrandLivreViewModel()
    {
        LoadGrandLivreCommand = new RelayCommand<object>(_ => _ = LoadAsync());
    }

    private async Task LoadAsync()
    {
        Message = null;
        Lignes.Clear();
        DenominationSociale = "";

        if (!int.TryParse(IdDossier, out int idDossier))
        {
            Message = "Veuillez saisir un identifiant de dossier valide.";
            return;
        }

        if (string.IsNullOrWhiteSpace(DateDebut) || string.IsNullOrWhiteSpace(DateFin))
        {
            Message = "Veuillez saisir les dates de début et de fin.";
            return;
        }

        IsLoading = true;
        try
        {
            var (lignes, denomination, error) =
                await _grandLivreService.GetGrandLivreAsync(idDossier, DateDebut, DateFin);

            if (!string.IsNullOrEmpty(error))
            {
                Message = error;
                return;
            }

            DenominationSociale = denomination;

            if (lignes.Count == 0)
            {
                Message = "Aucune écriture trouvée pour cette période.";
                return;
            }

            foreach (var ligne in lignes)
                Lignes.Add(ligne);
        }
        finally
        {
            IsLoading = false;
        }
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
