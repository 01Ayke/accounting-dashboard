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
        private DossierFactice? _currentDossier;

        public MainWindow()
        {
            InitializeComponent();
            MainContent.Content = new LoginView();
        }

        private void ShowAppShell(bool visible)
        {
            AppShell.Visibility = visible ? Visibility.Visible : Visibility.Collapsed;
        }

        // Bascule entre Zone 1 (accueil) et Zone 2 (intérieur dossier)
        private void SetNavZone(int zone)
        {
            NavZone1.Visibility = zone == 1 ? Visibility.Visible : Visibility.Collapsed;
            NavZone2.Visibility = zone == 2 ? Visibility.Visible : Visibility.Collapsed;

            if (zone == 1)
            {
                // Restrictions de rôle Zone 1
                BtnAdmin.Visibility = SessionManager.IsAdmin
                    ? Visibility.Visible : Visibility.Collapsed;
                BtnGestionDossier.Visibility = (SessionManager.IsAdmin || SessionManager.IsExpert || SessionManager.IsComptable)
                    ? Visibility.Visible : Visibility.Collapsed;
            }
        }

        // ── Handlers navbar ──────────────────────────────────────────────

        private void NavAccueil_Click(object sender, RoutedEventArgs e)
            => NavigateBackToHome();

        private void OpenContextMenu(object sender)
        {
            if (sender is Button btn && btn.ContextMenu != null)
            {
                btn.ContextMenu.PlacementTarget = btn;
                btn.ContextMenu.Placement = PlacementMode.Bottom;
                btn.ContextMenu.IsOpen = true;
            }
        }

        private void AdminMenu_Click(object sender, RoutedEventArgs e)          => OpenContextMenu(sender);
        private void GestionDossierMenu_Click(object sender, RoutedEventArgs e) => OpenContextMenu(sender);
        private void DossierAdminMenu_Click(object sender, RoutedEventArgs e)   => OpenContextMenu(sender);
        private void ComptabiliteMenu_Click(object sender, RoutedEventArgs e)   => OpenContextMenu(sender);
        private void ConsultationMenu_Click(object sender, RoutedEventArgs e)   => OpenContextMenu(sender);

        // Zone 1
        private void NavNouvelUtilisateur_Click(object sender, RoutedEventArgs e) => NavigateToUserManagement(0);
        private void NavNouveauRole_Click(object sender, RoutedEventArgs e)        => NavigateToUserManagement(1);
        private void NavNouveauDossier_Click(object sender, RoutedEventArgs e)     => NavigateToCreateDossier();

        // Zone 2
        private void NavNouveauJournal_Click(object sender, RoutedEventArgs e)
            => NavigateToNouveauJournal();

        private void NavSaisie_Click(object sender, RoutedEventArgs e)
        {
            ShowAppShell(true);
            MainContent.Content = new SaisieEcritureView();
        }

        private void NavNouveauTiers_Click(object sender, RoutedEventArgs e)
        {
            ShowAppShell(true);
            MainContent.Content = new NouveauTiersView();
        }

        private void NavGrandLivre_Click(object sender, RoutedEventArgs e)
            => NavigateToGrandLivre();

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
            SetNavZone(1);
            _homeView ??= new HomeView();
            MainContent.Content = _homeView;
        }

        public void NavigateToDossierDetail(DossierFactice dossier)
        {
            ShowAppShell(true);
            _currentDossier = dossier;
            SessionManager.CurrentDossierId   = dossier.IdDossier;
            SessionManager.CurrentDossierName = dossier.DenominationSociale;
            SetNavZone(2);
            var viewModel = new DossierDetailViewModel { Dossier = dossier };
            var dashboardView = new DashboardView { DataContext = viewModel };
            MainContent.Content = dashboardView;
        }

        public void NavigateBackToHome()
        {
            _currentDossier = null;
            SessionManager.CurrentDossierId = null; // Nettoyer
            ShowAppShell(true);
            SetNavZone(1);
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

        public void NavigateToUserManagement(int tab = 0)
        {
            ShowAppShell(true);
            MainContent.Content = new UserManagementView(tab);
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

        public void NavigateBackToDossierDetail()
        {
            if (_currentDossier != null)
                NavigateToDossierDetail(_currentDossier);
            else
                NavigateBackToHome();
        }

        public void NavigateToNouveauJournal()
        {
            ShowAppShell(true);
            MainContent.Content = new NouveauJournalView();
        }

        public void NavigateToLogin()
        {
            ShowAppShell(false);
            _homeView = null;
            _currentDossier = null;
            MainContent.Content = new LoginView();
        }
    }
}
