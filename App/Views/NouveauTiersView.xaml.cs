using System.Windows;
using System.Windows.Controls;
using App.ViewModels;

namespace App.Views;

public partial class NouveauTiersView : UserControl
{
    public NouveauTiersView()
    {
        InitializeComponent();
        Loaded += OnLoaded;
    }

    private async void OnLoaded(object sender, RoutedEventArgs e)
    {
        if (DataContext is NouveauTiersViewModel vm)
            await vm.InitAsync();
    }
}
