using App.Helpers;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using App.Services;

namespace App.ViewModels;
public class LoginViewModel : INotifyPropertyChanged
{
    private readonly AuthService _authService = new AuthService(useTestMode: false);
    
    public event Action? LoginSucceeded;
    public event PropertyChangedEventHandler? PropertyChanged;

    private string? _email;
    private string? _password;
    private string? _errorMessage;

    public string? Email
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

    public string? Password
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

    public string? ErrorMessage
    {
        get => _errorMessage;
        set
        {
            if (_errorMessage != value)
            {
                _errorMessage = value;
                OnPropertyChanged();
            }
        }
    }

    public RelayCommand<object> LoginCommand { get; }

    public LoginViewModel()
    {
        LoginCommand = new RelayCommand<object>(_ => _ = Login());
    }

    public async Task Login()
    {
        ErrorMessage = null; // Efface l'erreur précédente

        if (string.IsNullOrWhiteSpace(Email) || string.IsNullOrWhiteSpace(Password))
        {
            ErrorMessage = "Veuillez remplir tous les champs";
            return;
        }

        try
        {
            var response = await _authService.LoginAsync(Email, Password);
            
            if (response?.Status == "success")
            {
                // Déclenche l'événement pour naviguer
                LoginSucceeded?.Invoke();
            }
            else
            {
                ErrorMessage = "Mauvais identifiant ou mauvais mot de passe";
            }
        }
        catch (Exception ex)
        {
            ErrorMessage = "Erreur lors de la connexion : " + ex.Message;
        }
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}
