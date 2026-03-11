using System.Windows;
using System.Windows.Controls;

namespace App.Views;

public partial class GrandLivreView : UserControl
{
    public GrandLivreView()
    {
        InitializeComponent();
    }

    private void BackButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        mainWindow?.NavigateBackToHome();
    }
}