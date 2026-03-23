using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class UserManagementViewModel : INotifyPropertyChanged
{
    private readonly UserService _userService = new UserService();

    // ─── Création utilisateur ─────────────────────────────────────────────────

    private string _email = "";
    private string _password = "";
    private string _confirmPassword = "";
    private string _nom = "";
    private string _prenom = "";

    public string Email
    {
        get => _email;
        set { if (_email != value) { _email = value; OnPropertyChanged(); } }
    }

    public string Password
    {
        get => _password;
        set { if (_password != value) { _password = value; OnPropertyChanged(); } }
    }

    public string ConfirmPassword
    {
        get => _confirmPassword;
        set { if (_confirmPassword != value) { _confirmPassword = value; OnPropertyChanged(); } }
    }

    public string Nom
    {
        get => _nom;
        set { if (_nom != value) { _nom = value; OnPropertyChanged(); } }
    }

    public string Prenom
    {
        get => _prenom;
        set { if (_prenom != value) { _prenom = value; OnPropertyChanged(); } }
    }

    // ─── Attribution de rôle ─────────────────────────────────────────────────

    private string _userId = "";
    private string _selectedRole = "";

    public string UserId
    {
        get => _userId;
        set { if (_userId != value) { _userId = value; OnPropertyChanged(); } }
    }

    public string SelectedRole
    {
        get => _selectedRole;
        set { if (_selectedRole != value) { _selectedRole = value; OnPropertyChanged(); } }
    }

    // Rôles disponibles dans le backend
    public ObservableCollection<string> AvailableRoles { get; } = new()
    {
        "USER", "ADMIN", "EXPERT", "COMPTA_CONF", "COMPTA_JR",
        "ASSISTANT", "STAGIAIRE", "CLIENT"
    };

    // ─── Messages ─────────────────────────────────────────────────────────────

    private string? _createUserMessage;
    private string? _assignRoleMessage;
    private bool _createUserSuccess;
    private bool _assignRoleSuccess;

    public string? CreateUserMessage
    {
        get => _createUserMessage;
        set { if (_createUserMessage != value) { _createUserMessage = value; OnPropertyChanged(); } }
    }

    public string? AssignRoleMessage
    {
        get => _assignRoleMessage;
        set { if (_assignRoleMessage != value) { _assignRoleMessage = value; OnPropertyChanged(); } }
    }

    public bool CreateUserSuccess
    {
        get => _createUserSuccess;
        set { if (_createUserSuccess != value) { _createUserSuccess = value; OnPropertyChanged(); } }
    }

    public bool AssignRoleSuccess
    {
        get => _assignRoleSuccess;
        set { if (_assignRoleSuccess != value) { _assignRoleSuccess = value; OnPropertyChanged(); } }
    }

    // ─── Commandes ────────────────────────────────────────────────────────────

    public RelayCommand<object> CreateUserCommand { get; }
    public RelayCommand<object> AssignRoleCommand { get; }

    public event PropertyChangedEventHandler? PropertyChanged;

    public UserManagementViewModel()
    {
        CreateUserCommand = new RelayCommand<object>(_ => _ = CreateUserAsync());
        AssignRoleCommand = new RelayCommand<object>(_ => _ = AssignRoleAsync());
    }

    private async Task CreateUserAsync()
    {
        CreateUserMessage = null;
        CreateUserSuccess = false;

        if (string.IsNullOrWhiteSpace(Email) ||
            string.IsNullOrWhiteSpace(Password) ||
            string.IsNullOrWhiteSpace(ConfirmPassword) ||
            string.IsNullOrWhiteSpace(Nom) ||
            string.IsNullOrWhiteSpace(Prenom))
        {
            CreateUserMessage = "Veuillez remplir tous les champs.";
            return;
        }

        if (Password != ConfirmPassword)
        {
            CreateUserMessage = "Les mots de passe ne correspondent pas.";
            return;
        }

        var request = new CreateUserRequest
        {
            Email    = Email,
            Password = Password,
            Nom      = Nom,
            Prenom   = Prenom
        };

        var (success, msg) = await _userService.CreateUserAsync(request);
        CreateUserSuccess = success;
        CreateUserMessage = msg;

        if (success)
        {
            Email = ""; Password = ""; ConfirmPassword = ""; Nom = ""; Prenom = "";
        }
    }

    private async Task AssignRoleAsync()
    {
        AssignRoleMessage = null;
        AssignRoleSuccess = false;

        if (!int.TryParse(UserId, out var userId))
        {
            AssignRoleMessage = "ID utilisateur invalide (entier requis).";
            return;
        }

        if (string.IsNullOrWhiteSpace(SelectedRole))
        {
            AssignRoleMessage = "Veuillez sélectionner un rôle.";
            return;
        }

        var (success, msg) = await _userService.AssignRoleAsync(userId, SelectedRole);
        AssignRoleSuccess = success;
        AssignRoleMessage = msg;
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
