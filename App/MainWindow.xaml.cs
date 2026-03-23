using System.Windows;
using App.Models;
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

        public void NavigateToHome()
        {
            _homeView ??= new HomeView();
            MainContent.Content = _homeView;
        }

        public void NavigateToDossierDetail(DossierFactice dossier)
        {
            // var vm = new DossierDetailViewModel
            // {
            //     Dossier = dossier
            // };

            // var view = new DossierDetailView
            // {
            //     DataContext = vm
            // };

            // MainContent.Content = view;
			var viewModel = new DossierDetailViewModel { Dossier = dossier };
            var dashboardView = new DashboardView { DataContext = viewModel };
            MainContent.Content = dashboardView;
        }

        public void NavigateBackToHome()
        {
            _homeView ??= new HomeView();
            MainContent.Content = _homeView;
        }

        public void NavigateToCreateDossier()
        {
            MainContent.Content = new CreateDossierView();
        }

        public void NavigateToAssignDossier()
        {
            MainContent.Content = new AssignDossierView();
        }

        public void NavigateToUserManagement()
        {
            MainContent.Content = new UserManagementView();
        }

        public void NavigateToCreateCompte()
        {
            MainContent.Content = new CreateCompteView();
        }

        public void NavigateToGrandLivre()
        {
            MainContent.Content = new GrandLivreView();
        }

        public void NavigateToDocuments()
        {
            MainContent.Content = new DocumentView();
        }
    }
}
