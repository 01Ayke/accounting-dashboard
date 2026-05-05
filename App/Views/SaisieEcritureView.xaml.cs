using System.Windows;
using System.Windows.Controls;
using App.ViewModels;

namespace App.Views;

public partial class SaisieEcritureView : UserControl
{
    public SaisieEcritureView()
    {
        InitializeComponent();
        Loaded += OnLoaded;
    }

    private async void OnLoaded(object sender, RoutedEventArgs e)
    {
        if (DataContext is SaisieEcritureViewModel vm)
        {
            await vm.InitAsync();
        }
    }

    private void BackButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        mainWindow?.NavigateBackToDossierDetail();
    }
}
