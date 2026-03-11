using System.Windows;
using System.Windows.Controls;

namespace App.Views;

public partial class CreateCompteView : UserControl
{
    public CreateCompteView()
    {
        InitializeComponent();
    }

    private void BackButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        mainWindow?.NavigateBackToHome();
    }
}