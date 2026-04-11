using System.ComponentModel;
using System.Runtime.CompilerServices;
using App.Helpers;

namespace App.ViewModels;

public class NouveauJournalViewModel : INotifyPropertyChanged
{
    private string _nom = "";
    private string _code = "";
    private string _commentaire = "";
    private string? _errorMessage;
    private string? _successMessage;

    public string Nom
    {
        get => _nom;
        set { if (_nom != value) { _nom = value; OnPropertyChanged(); } }
    }

    public string Code
    {
        get => _code;
        set { if (_code != value) { _code = value; OnPropertyChanged(); } }
    }

    public string Commentaire
    {
        get => _commentaire;
        set { if (_commentaire != value) { _commentaire = value; OnPropertyChanged(); } }
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

    public RelayCommand<object> ValiderCommand { get; }

    public NouveauJournalViewModel()
    {
        ValiderCommand = new RelayCommand<object>(_ => Valider());
    }

    private void Valider()
    {
        ErrorMessage = null;
        SuccessMessage = null;

        if (string.IsNullOrWhiteSpace(Nom))
        {
            ErrorMessage = "Le nom du journal est obligatoire.";
            return;
        }

        if (string.IsNullOrWhiteSpace(Code))
        {
            ErrorMessage = "Le code du journal est obligatoire.";
            return;
        }

        // TODO : appel API création journal
        SuccessMessage = $"Journal « {Nom} » ({Code}) créé avec succès.";
    }

    public event PropertyChangedEventHandler? PropertyChanged;
    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
