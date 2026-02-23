using App.Helpers;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using App.Services;

namespace App.ViewModels;
public class LoginViewModel
{
    private readonly AuthService _authService = new AuthService();
    
    public event Action? LoginSucceeded;

    public string? Email { get; set; }
    public string? Password { get; set; }

    public RelayCommand<object> LoginCommand { get; }

    public LoginViewModel()
    {
        LoginCommand = new RelayCommand<object>(_ => _ = Login());
    }

    public async Task Login()
    {
        if (string.IsNullOrWhiteSpace(Email) || string.IsNullOrWhiteSpace(Password))
        {
            MessageBox.Show("Veuillez remplir tous les champs");
            return;
        }

        try
        {
            var response = await _authService.LoginAsync(Email, Password);
            if (response.Status == "success")
            {
                // Déclenche l'événement pour ouvrir la nouvelle fenêtre
                LoginSucceeded?.Invoke();
            }
            else
            {
                MessageBox.Show(response.Message);
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show("Erreur lors de la connexion : " + ex.Message);
        }
    }
}
