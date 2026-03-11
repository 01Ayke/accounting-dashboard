using System.Windows;
using System.Windows.Controls;
using App.Models;

namespace App.Views;

public partial class HomeView : UserControl
{
    public HomeView()
    {
        InitializeComponent();
    }

    private void Dossier_DoubleClick(object sender, System.Windows.Input.MouseButtonEventArgs e)
    {
        if (sender is ListBox listBox && listBox.SelectedItem is DossierFactice dossier)
        {
            var mainWindow = Window.GetWindow(this) as MainWindow;
            if (mainWindow != null)
            {
                mainWindow.NavigateToDossierDetail(dossier);
            }
        }
    }

    private void OpenContextMenu(object sender, RoutedEventArgs e)
    {
        if (sender is Button button && button.ContextMenu != null)
        {
            button.ContextMenu.PlacementTarget = button;
            button.ContextMenu.Placement = System.Windows.Controls.Primitives.PlacementMode.Bottom;
            button.ContextMenu.IsOpen = true;
        }
    }

    private void ResearchBox_KeyDown(object sender, System.Windows.Input.KeyEventArgs e)
    {
        if (e.Key == System.Windows.Input.Key.Tab && SuggestionsListBox.Items.Count > 0)
        {
            SuggestionsListBox.SelectedIndex = 0;
            if (SuggestionsListBox.SelectedItem is string suggestion)
            {
                ResearchBox.Text = suggestion;
                ResearchBox.CaretIndex = ResearchBox.Text.Length;
            }

            e.Handled = true;
        }
        else if (e.Key == System.Windows.Input.Key.Escape)
        {
            SuggestionsPopup.IsOpen = false;
            e.Handled = true;
        }
        else if (e.Key == System.Windows.Input.Key.Down && SuggestionsListBox.Items.Count > 0)
        {
            SuggestionsListBox.Focus();
            SuggestionsListBox.SelectedIndex = 0;
            e.Handled = true;
        }
    }

    private void SuggestionsListBox_MouseDown(object sender, System.Windows.Input.MouseButtonEventArgs e)
    {
        if (SuggestionsListBox.SelectedItem is string suggestion)
        {
            ResearchBox.Text = suggestion;
            SuggestionsListBox.Visibility = Visibility.Collapsed;
        }
    }

    private void AddButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        if (mainWindow != null)
        {
            mainWindow.NavigateToCreateDossier();
        }
    }

    private void AssignDossierButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        if (mainWindow != null)
        {
            mainWindow.NavigateToAssignDossier();
        }
    }

    private void UserManagementButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        if (mainWindow != null)
        {
            mainWindow.NavigateToUserManagement();
        }
    }

    private void CreateCompteButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        if (mainWindow != null)
        {
            mainWindow.NavigateToCreateCompte();
        }
    }

    private void GrandLivreButton_Click(object sender, RoutedEventArgs e)
    {
        var mainWindow = Window.GetWindow(this) as MainWindow;
        if (mainWindow != null)
        {
            mainWindow.NavigateToGrandLivre();
        }
    }
}