using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using System.Windows.Controls;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class UserManagementViewModel : INotifyPropertyChanged
{
    private readonly UserService _userService = new UserService();

    private string _email = "";
    private string _password = "";
    private string _confirmPassword = "";
    private string _userId = "";
    private ComboBoxItem? _selectedRole;
    private string? _message;

    public string Email
    {
        get => _email;
        set
        {
            if (_email != value)
            {
                _email = value;
                OnPropertyChanged();
            }
        }
    }

    public string Password
    {
        get => _password;
        set
        {
            if (_password != value)
            {
                _password = value;
                OnPropertyChanged();
            }
        }
    }

    public string ConfirmPassword
    {
        get => _confirmPassword;
        set
        {
            if (_confirmPassword != value)
            {
                _confirmPassword = value;
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

    public ComboBoxItem? SelectedRole
    {
        get => _selectedRole;
        set
        {
            if (_selectedRole != value)
            {
                _selectedRole = value;
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
        Message = null;

        if (string.IsNullOrWhiteSpace(Email) ||
            string.IsNullOrWhiteSpace(Password) ||
            string.IsNullOrWhiteSpace(ConfirmPassword))
        {
            Message = "Veuillez remplir tous les champs.";
            return;
        }

        if (Password != ConfirmPassword)
        {
            Message = "Les mots de passe ne correspondent pas.";
            return;
        }

        var request = new CreateUserRequest
        {
            Email = Email,
            Password = Password
        };

        var success = await _userService.CreateUserAsync(request);

        Message = success
            ? "Utilisateur créé avec succès."
            : "Erreur lors de la création de l'utilisateur.";
    }

    private async Task AssignRoleAsync()
    {
        Message = null;

        if (!int.TryParse(UserId, out var userId))
        {
            Message = "ID utilisateur invalide.";
            return;
        }

        if (SelectedRole?.Content == null)
        {
            Message = "Veuillez sélectionner un rôle.";
            return;
        }

        var request = new AssignRoleRequest
        {
            Role = SelectedRole.Content.ToString() ?? ""
        };

        var success = await _userService.AssignRoleAsync(userId, request);

        Message = success
            ? "Rôle attribué avec succès."
            : "Erreur lors de l'attribution du rôle.";
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}