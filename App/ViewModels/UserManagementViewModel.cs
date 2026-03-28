using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class UserManagementViewModel : INotifyPropertyChanged
{
    private readonly UserService _userService = new UserService();

    // ─── Liste des utilisateurs ───────────────────────────────────────────────

    public ObservableCollection<UserItem> Users { get; } = new();

    private UserItem? _selectedUser;
    public UserItem? SelectedUser
    {
        get => _selectedUser;
        set
        {
            if (_selectedUser != value)
            {
                _selectedUser = value;
                OnPropertyChanged();
                AssignRoleMessage = null;
                _ = LoadUserRolesAsync();
            }
        }
    }

    // ─── Rôles de l'utilisateur sélectionné ──────────────────────────────────

    public ObservableCollection<RoleCheckItem> UserRoles { get; } = new();

    public int ActiveRoleCount => UserRoles.Count(r => r.IsChecked);

    private bool _rolesLoading;
    public bool RolesLoading
    {
        get => _rolesLoading;
        set { if (_rolesLoading != value) { _rolesLoading = value; OnPropertyChanged(); } }
    }

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

    // Rôles à attribuer à la création
    public ObservableCollection<RoleCheckItem> NewUserRoles { get; } = new();

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

    private static readonly string[] AvailableRoleNames =
        { "USER", "ADMIN", "EXPERT", "COMPTA_CONF", "COMPTA_JR", "ASSISTANT", "STAGIAIRE", "CLIENT" };

    public UserManagementViewModel()
    {
        CreateUserCommand = new RelayCommand<object>(_ => _ = CreateUserAsync());
        AssignRoleCommand = new RelayCommand<object>(_ => _ = ApplyRolesAsync());

        // Initialiser les collections de rôles
        foreach (var role in AvailableRoleNames)
        {
            var userRole = new RoleCheckItem { Name = role };
            userRole.PropertyChanged += (_, _) => OnPropertyChanged(nameof(ActiveRoleCount));
            UserRoles.Add(userRole);

            NewUserRoles.Add(new RoleCheckItem { Name = role });
        }

        _ = LoadUsersAsync();
    }

    private async Task LoadUsersAsync()
    {
        var users = await _userService.GetUsersAsync();
        Users.Clear();
        foreach (var u in users)
            Users.Add(u);
    }

    private async Task LoadUserRolesAsync()
    {
        if (SelectedUser == null)
        {
            foreach (var r in UserRoles) r.IsChecked = false;
            return;
        }

        RolesLoading = true;
        try
        {
            var currentRoles = await _userService.GetUserRolesAsync(SelectedUser.Id);
            foreach (var item in UserRoles)
                item.IsChecked = currentRoles.Contains(item.Name);
            OnPropertyChanged(nameof(ActiveRoleCount));
        }
        finally
        {
            RolesLoading = false;
        }
    }

    private async Task ApplyRolesAsync()
    {
        AssignRoleMessage = null;
        AssignRoleSuccess = false;

        if (SelectedUser == null)
        {
            AssignRoleMessage = "Veuillez sélectionner un utilisateur.";
            return;
        }

        var roles = string.Join(",", UserRoles.Where(r => r.IsChecked).Select(r => r.Name));

        var (success, msg) = await _userService.AssignRoleAsync(SelectedUser.Id, roles);
        AssignRoleSuccess = success;
        AssignRoleMessage = msg;

        if (success)
            await LoadUserRolesAsync();
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
            var selectedRoles = NewUserRoles.Where(r => r.IsChecked).ToList();

            if (selectedRoles.Any())
            {
                // Rafraîchir la liste pour obtenir l'ID du nouvel utilisateur
                await LoadUsersAsync();
                var createdEmail = Email;
                var newUser = Users.FirstOrDefault(u =>
                    u.Mail.Equals(createdEmail, System.StringComparison.OrdinalIgnoreCase));

                if (newUser != null)
                {
                    var rolesStr = string.Join(",", selectedRoles.Select(r => r.Name));
                    var (roleSuccess, roleMsg) = await _userService.AssignRoleAsync(newUser.Id, rolesStr);
                    CreateUserMessage = roleSuccess
                        ? $"Compte créé et rôles attribués."
                        : $"Compte créé mais erreur rôles : {roleMsg}";
                }
                else
                {
                    CreateUserMessage = "Compte créé. Impossible d'attribuer les rôles automatiquement.";
                }
            }

            Email = ""; Password = ""; ConfirmPassword = ""; Nom = ""; Prenom = "";
            foreach (var r in NewUserRoles) r.IsChecked = false;

            await LoadUsersAsync();
        }
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
