using System;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class LoginViewModel : INotifyPropertyChanged
{
    private readonly AuthService _authService = new AuthService(useTestMode: false);
    private readonly UserService _userService = new UserService();

    public event Action? LoginSucceeded;
    public event PropertyChangedEventHandler? PropertyChanged;

    private string? _email;
    private string? _password;
    private string? _errorMessage;
    private bool _isLoading;

    public string? Email
    {
        get => _email;
        set { if (_email != value) { _email = value; OnPropertyChanged(); } }
    }

    public string? Password
    {
        get => _password;
        set { if (_password != value) { _password = value; OnPropertyChanged(); } }
    }

    public string? ErrorMessage
    {
        get => _errorMessage;
        set { if (_errorMessage != value) { _errorMessage = value; OnPropertyChanged(); } }
    }

    public bool IsLoading
    {
        get => _isLoading;
        set { if (_isLoading != value) { _isLoading = value; OnPropertyChanged(); } }
    }

    public RelayCommand<object> LoginCommand { get; }

    public LoginViewModel()
    {
        LoginCommand = new RelayCommand<object>(_ => _ = Login());
    }

    public async Task Login()
    {
        ErrorMessage = null;

        if (string.IsNullOrWhiteSpace(Email) || string.IsNullOrWhiteSpace(Password))
        {
            ErrorMessage = "Veuillez remplir tous les champs.";
            return;
        }

        IsLoading = true;
        try
        {
            var response = await _authService.LoginAsync(Email, Password);

            if (response?.Status == "success")
            {
                // Stocker le token et l'email pour toutes les requêtes suivantes
                SessionManager.Token = response.Token;
                SessionManager.CurrentUserEmail = Email;
                System.Diagnostics.Debug.WriteLine($"LoginViewModel: Token sID stocké = {SessionManager.Token}");

                // Vérifier si l'utilisateur est admin
                SessionManager.IsAdmin = await _userService.CheckIsAdminAsync();
                System.Diagnostics.Debug.WriteLine($"LoginViewModel: IsAdmin = {SessionManager.IsAdmin}");

                LoginSucceeded?.Invoke();
            }
            else
            {
                ErrorMessage = response?.Message ?? "Identifiant ou mot de passe incorrect.";
            }
        }
        catch (Exception ex)
        {
            ErrorMessage = "Erreur lors de la connexion : " + ex.Message;
        }
        finally
        {
            IsLoading = false;
        }
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
