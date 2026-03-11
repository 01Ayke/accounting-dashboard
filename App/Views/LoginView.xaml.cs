using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using App.ViewModels;

namespace App.Views
{
    public partial class LoginView : UserControl
    {
        private readonly Random _rand = new();

        public LoginView()
        {
            InitializeComponent();

            var vm = new LoginViewModel();
            vm.LoginSucceeded += () =>
            {
                var mainWindow = Window.GetWindow(this) as MainWindow;
                if (mainWindow != null)
                {
                    mainWindow.NavigateToHome();
                }
            };

            DataContext = vm;
        }

        private void LoginView_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                if (DataContext is LoginViewModel vm)
                {
                    if (vm.LoginCommand.CanExecute(null))
                        vm.LoginCommand.Execute(null);
                }

                e.Handled = true;
            }
        }

        private void LoginView_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Return)
            {
                if (DataContext is LoginViewModel vm)
                {
                    if (vm.LoginCommand.CanExecute(null))
                        vm.LoginCommand.Execute(null);
                }

                e.Handled = true;
            }
        }

        private void PasswordBox_PasswordChanged(object sender, RoutedEventArgs e)
        {
            PasswordPlaceholder.Visibility =
                string.IsNullOrEmpty(PasswordBox.Password)
                    ? Visibility.Visible
                    : Visibility.Collapsed;

            if (DataContext is LoginViewModel vm)
            {
                vm.Password = ((PasswordBox)sender).Password;
            }
        }
    }
}