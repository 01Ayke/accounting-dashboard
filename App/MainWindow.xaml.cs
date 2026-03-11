using System.Windows;
using App.Models;
using App.ViewModels;
using App.Views;

namespace App
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            MainContent.Content = new LoginView();
        }

        public void NavigateToHome()
        {
            MainContent.Content = new HomeView();
        }

        public void NavigateToDossierDetail(DossierFactice dossier)
        {
            var vm = new DossierDetailViewModel
            {
                Dossier = dossier
            };

            var view = new DossierDetailView
            {
                DataContext = vm
            };

            MainContent.Content = view;
        }

        public void NavigateBackToHome()
        {
            MainContent.Content = new HomeView();
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
    }
}