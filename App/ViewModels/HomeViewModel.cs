using System.Collections.ObjectModel;
using System.Windows.Input;
using App.Helpers;
using App.Models;

namespace App.ViewModels;
public class HomeViewModel
{
    public ObservableCollection<DossierFactice> Dossiers { get; }
    public RelayCommand<DossierFactice> SupprimerDossierCommand { get; }

    public HomeViewModel()
    {
        Dossiers = new ObservableCollection<DossierFactice>
        {
            new DossierFactice { NomEntreprise = "FOYER SARL", Statut = "Actif", Siret = "21022000", Prenom = "Benjamin", Nom = "Basset", EmailContact = "bbasset@bordeaux-inp.fr", Secteur = "Orphelin"},
        };

        SupprimerDossierCommand = new RelayCommand<DossierFactice>(dossier =>
        {
            if (dossier != null && Dossiers.Contains(dossier))
                Dossiers.Remove(dossier); // ObservableCollection met à jour la ListBox automatiquement
        });
    }
}
