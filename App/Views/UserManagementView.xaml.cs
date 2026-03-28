using System.Windows;
using System.Windows.Controls;
using App.ViewModels;

namespace App.Views;

public partial class UserManagementView : UserControl
{
    public UserManagementView()
    {
        InitializeComponent();
    }

    private void BackButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        mainWindow?.NavigateBackToHome();
    }

    // PasswordBox ne supporte pas le binding direct, on passe par le code-behind
    private void PwdBox_PasswordChanged(object sender, RoutedEventArgs e)
    {
        if (DataContext is UserManagementViewModel vm)
            vm.Password = PwdBox.Password;
    }

    private void ConfirmPwdBox_PasswordChanged(object sender, RoutedEventArgs e)
    {
        if (DataContext is UserManagementViewModel vm)
            vm.ConfirmPassword = ConfirmPwdBox.Password;
    }
}
