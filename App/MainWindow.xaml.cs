using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

using App.Views;
using App.Models;
using App.ViewModels;

namespace App
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();

            // Page affichée au démarrage
            MainContent.Content = new LoginView();
        }

        public void NavigateToHome()
        {
            MainContent.Content = new HomeView();
        }

        public void NavigateToDashboard(DossierFactice dossier)
        {
            var viewModel = new DossierDetailViewModel { Dossier = dossier };
            var dashboardView = new DashboardView { DataContext = viewModel };
            MainContent.Content = dashboardView;
        }

        public void NavigateBack()
        {
            MainContent.Content = new HomeView();
        }
    }
}
