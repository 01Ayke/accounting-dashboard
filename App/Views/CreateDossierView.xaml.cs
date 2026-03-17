using System.Windows;
using System.Windows.Controls;

namespace App.Views;

public partial class CreateDossierView : UserControl
{
    public CreateDossierView()
    {
        InitializeComponent();
    }

    private void BackButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        if (mainWindow != null)
        {
            mainWindow.NavigateBackToHome();
        }
    }
}