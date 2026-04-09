using System.Windows;
using System.Windows.Controls;
using App.ViewModels;

namespace App.Views;

public partial class UserManagementView : UserControl
{
    public UserManagementView(int initialTab = 0)
    {
        InitializeComponent();
        MainTabControl.SelectedIndex = initialTab;
    }

    private void BackButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        mainWindow?.NavigateBackToHome();
    }

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

    private void PwdClear_TextChanged(object sender, TextChangedEventArgs e)
    {
        if (DataContext is UserManagementViewModel vm)
            vm.Password = PwdClear.Text;
    }

    private void ConfirmPwdClear_TextChanged(object sender, TextChangedEventArgs e)
    {
        if (DataContext is UserManagementViewModel vm)
            vm.ConfirmPassword = ConfirmPwdClear.Text;
    }

    private void TogglePwd_Click(object sender, RoutedEventArgs e)
    {
        if (PwdBox.Visibility == Visibility.Visible)
        {
            PwdClear.Text = PwdBox.Password;
            PwdBox.Visibility = Visibility.Collapsed;
            PwdClear.Visibility = Visibility.Visible;
            TogglePwdBtn.Content = "🙈";
        }
        else
        {
            PwdBox.Password = PwdClear.Text;
            PwdClear.Visibility = Visibility.Collapsed;
            PwdBox.Visibility = Visibility.Visible;
            TogglePwdBtn.Content = "👁";
        }
    }

    private void ToggleConfirmPwd_Click(object sender, RoutedEventArgs e)
    {
        if (ConfirmPwdBox.Visibility == Visibility.Visible)
        {
            ConfirmPwdClear.Text = ConfirmPwdBox.Password;
            ConfirmPwdBox.Visibility = Visibility.Collapsed;
            ConfirmPwdClear.Visibility = Visibility.Visible;
            ToggleConfirmPwdBtn.Content = "🙈";
        }
        else
        {
            ConfirmPwdBox.Password = ConfirmPwdClear.Text;
            ConfirmPwdClear.Visibility = Visibility.Collapsed;
            ConfirmPwdBox.Visibility = Visibility.Visible;
            ToggleConfirmPwdBtn.Content = "👁";
        }
    }
}
