using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using App.Models;
using App.Services;
using App.ViewModels;
using App.Views;

namespace App
{
    public partial class MainWindow : Window
    {
        private HomeView? _homeView;

        public MainWindow()
        {
            InitializeComponent();
            MainContent.Content = new LoginView();
        }

        private void ShowAppShell(bool visible)
        {
            AppShell.Visibility = visible ? Visibility.Visible : Visibility.Collapsed;
        }

        // ── Handlers navbar ──────────────────────────────────────────────

        private void NavAccueil_Click(object sender, RoutedEventArgs e)
            => NavigateBackToHome();

        private void AdminMenu_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button btn && btn.ContextMenu != null)
            {
                btn.ContextMenu.PlacementTarget = btn;
                btn.ContextMenu.Placement = PlacementMode.Bottom;
                btn.ContextMenu.IsOpen = true;
            }
        }

        private void GestionDossierMenu_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button btn && btn.ContextMenu != null)
            {
                btn.ContextMenu.PlacementTarget = btn;
                btn.ContextMenu.Placement = PlacementMode.Bottom;
                btn.ContextMenu.IsOpen = true;
            }
        }

        private void NavNouvelUtilisateur_Click(object sender, RoutedEventArgs e)
            => NavigateToUserManagement();

        private void NavNouveauRole_Click(object sender, RoutedEventArgs e)
            => NavigateToUserManagement();

        private void NavNouveauDossier_Click(object sender, RoutedEventArgs e)
            => NavigateToCreateDossier();

        private async void NavDeconnexion_Click(object sender, RoutedEventArgs e)
        {
            var authService = new AuthService();
            await authService.LogoutAsync();
            NavigateToLogin();
        }

        // ── Méthodes de navigation ───────────────────────────────────────

        public void NavigateToHome()
        {
            ShowAppShell(true);
            _homeView ??= new HomeView();
            MainContent.Content = _homeView;
        }

        public void NavigateToDossierDetail(DossierFactice dossier)
        {
            ShowAppShell(true);
            var viewModel = new DossierDetailViewModel { Dossier = dossier };
            var dashboardView = new DashboardView { DataContext = viewModel };
            MainContent.Content = dashboardView;
        }

        public void NavigateBackToHome()
        {
            ShowAppShell(true);
            _homeView ??= new HomeView();
            MainContent.Content = _homeView;
            if (_homeView.DataContext is HomeViewModel vm)
                _ = vm.RefreshAsync();
        }

        public void NavigateToCreateDossier()
        {
            ShowAppShell(true);
            MainContent.Content = new CreateDossierView();
        }

        public void NavigateToAssignDossier()
        {
            ShowAppShell(true);
            MainContent.Content = new AssignDossierView();
        }

        public void NavigateToUserManagement()
        {
            ShowAppShell(true);
            MainContent.Content = new UserManagementView();
        }

        public void NavigateToCreateCompte()
        {
            ShowAppShell(true);
            MainContent.Content = new CreateCompteView();
        }

        public void NavigateToGrandLivre()
        {
            ShowAppShell(true);
            MainContent.Content = new GrandLivreView();
        }

        public void NavigateToLogin()
        {
            ShowAppShell(false);
            _homeView = null;
            MainContent.Content = new LoginView();
        }
    }
}
