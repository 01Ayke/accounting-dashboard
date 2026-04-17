using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
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

    private readonly App.Services.JournalService _journalService = new();

    public NouveauJournalViewModel()
    {
        ValiderCommand = new RelayCommand<object>(_ => _ = ValiderAsync());
    }

    private async Task ValiderAsync()
    {
        ErrorMessage = null;
        SuccessMessage = null;

        if (string.IsNullOrWhiteSpace(Nom))
        {
            ErrorMessage = "Le nom du journal est obligatoire.";
            return;
        }

        if (string.IsNullOrWhiteSpace(Code) || Code.Trim().Length != 5)
        {
            ErrorMessage = "Le code du journal est obligatoire et doit faire exactement 5 caractères.";
            return;
        }

        if (string.IsNullOrWhiteSpace(Commentaire))
        {
            ErrorMessage = "Le commentaire est obligatoire.";
            return;
        }

        var dossierId = App.Services.SessionManager.CurrentDossierId;
        if (!dossierId.HasValue)
        {
            ErrorMessage = "Erreur : Impossible d'identifier le dossier en cours.";
            return;
        }

        // Pour l'instant on met un IdTypeJournal factice par défaut (ex: 1)
        // car le formulaire XAML ne permet pas encore de choisir le type.
        int defaultTypeJournal = 1;

        var (success, msg) = await _journalService.CreateJournalAsync(
            dossierId.Value, 
            defaultTypeJournal, 
            Code, 
            Nom, 
            Commentaire
        );

        if (success)
        {
            SuccessMessage = msg;
            // Optionnel: Vider les champs après création
            Nom = "";
            Code = "";
            Commentaire = "";
        }
        else
        {
            ErrorMessage = msg;
        }
    }

    public event PropertyChangedEventHandler? PropertyChanged;
    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
