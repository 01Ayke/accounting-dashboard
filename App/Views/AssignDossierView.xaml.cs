using System.Windows;
using System.Windows.Controls;

namespace App.Views;

public partial class AssignDossierView : UserControl
{
    public AssignDossierView()
    {
        InitializeComponent();
    }

    private void BackButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        mainWindow?.NavigateBackToHome();
    }
}