using System.Windows;
using System.Windows.Controls;

namespace App.Views;

public partial class DocumentView : UserControl
{
    public DocumentView()
    {
        InitializeComponent();
    }

    private void BackButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        mainWindow?.NavigateBackToHome();
    }
}
