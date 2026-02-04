using App.Helpers;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace App.ViewModels;

public class LoginViewModel : INotifyPropertyChanged
{
    private string _username = "";
    private string _password = "";
    private string _message = "";

    public string Username
    {
        get => _username;
        set { _username = value; OnPropertyChanged(); }
    }

    public string Password
    {
        get => _password;
        set { _password = value; OnPropertyChanged(); }
    }

    public string Message
    {
        get => _message;
        set { _message = value; OnPropertyChanged(); }
    }

    public ICommand LoginCommand { get; }

    public LoginViewModel()
    {
        LoginCommand = new RelayCommand(Login);
    }

    private void Login()
    {
        // Exemple basique : remplacer par vérif réelle / Service
        // Il faudra appeler une fonction dans Services qui enverra une requête au back
        if (Username == "0000" && Password == "0000")
        {
            Application.Current.Dispatcher.Invoke(() =>
            {
                if (Application.Current.MainWindow is MainWindow mainWindow)
                {
                    mainWindow.NavigateToHome();
                }
            });
        }
        else
        {
            Message = "Nom d’utilisateur ou mot de passe incorrect.";
        }
    }
    
    


    
    public event PropertyChangedEventHandler? PropertyChanged;
    private void OnPropertyChanged([CallerMemberName] string? name = null) 
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}