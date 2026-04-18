using System.Windows;
using System.Windows.Controls;
using App.ViewModels;

namespace App.Views;

public partial class SaisieEcritureView : UserControl
{
    public SaisieEcritureView()
    {
        InitializeComponent();
        // S'abonner à Loaded pour charger les comptes APRÈS que la session soit disponible
        Loaded += OnLoaded;
    }

    private async void OnLoaded(object sender, RoutedEventArgs e)
    {
        if (DataContext is SaisieEcritureViewModel vm)
        {
            await vm.InitAsync();
        }
    }
}
