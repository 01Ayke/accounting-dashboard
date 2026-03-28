using System.Windows;
using System.Windows.Controls;
using App.ViewModels;

namespace App.Views;

public partial class CreateDossierView : UserControl
{
    public CreateDossierView()
    {
        InitializeComponent();
        Loaded += OnLoaded;
    }

    private void OnLoaded(object sender, RoutedEventArgs e)
    {
        if (DataContext is CreateDossierViewModel vm)
        {
            vm.DossierCree += OnDossierCree;
        }
    }

    private void OnDossierCree(object? sender, System.EventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        mainWindow?.NavigateBackToHome();
    }

    private void BackButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        mainWindow?.NavigateBackToHome();
    }
}
