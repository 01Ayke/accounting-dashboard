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

    private string _numeroCompte = "";
    private string _dateDebut = "";
    private string _dateFin = "";
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

    public string DateDebut
    {
        get => _dateDebut;
        set
        {
            if (_dateDebut != value)
            {
                _dateDebut = value;
                OnPropertyChanged();
            }
        }
    }

    public string DateFin
    {
        get => _dateFin;
        set
        {
            if (_dateFin != value)
            {
                _dateFin = value;
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

        if (string.IsNullOrWhiteSpace(NumeroCompte))
        {
            Message = "Veuillez saisir un numéro de compte.";
            return;
        }

        var lignes = await _grandLivreService.GetGrandLivreAsync(NumeroCompte, DateDebut, DateFin);

        if (lignes == null || lignes.Count == 0)
        {
            Message = "Aucune donnée trouvée.";
            return;
        }

        foreach (var ligne in lignes)
        {
            Lignes.Add(ligne);
        }
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}