using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows.Input;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;
public class HomeViewModel : INotifyPropertyChanged
{
    private DossierFactice? _dossierSelectionne;
    private string? _searchText;
    private ObservableCollection<DossierFactice> _dossiersFiltrés;
    private ObservableCollection<string> _suggestionsRecherche;
    private bool _showSuggestions;
    private int _selectedSortIndex = -1;
    private readonly DossierService _dossierService = new DossierService();

    public ObservableCollection<DossierFactice> Dossiers { get; }
    
    public ObservableCollection<DossierFactice> DossiersFiltrés
    {
        get => _dossiersFiltrés;
        set
        {
            if (_dossiersFiltrés != value)
            {
                _dossiersFiltrés = value;
                OnPropertyChanged();
            }
        }
    }

    public ObservableCollection<string> SuggestionsRecherche
    {
        get => _suggestionsRecherche;
        set
        {
            if (_suggestionsRecherche != value)
            {
                _suggestionsRecherche = value;
                OnPropertyChanged();
            }
        }
    }

    public bool ShowSuggestions
    {
        get => _showSuggestions;
        set
        {
            if (_showSuggestions != value)
            {
                _showSuggestions = value;
                OnPropertyChanged();
            }
        }
    }

    public string? SearchText
    {
        get => _searchText;
        set
        {
            if (_searchText != value)
            {
                _searchText = value;
                OnPropertyChanged();
                FiltrerEtTrier();
                GénérerSuggestions();
            }
        }
    }

    public int SelectedSortIndex
    {
        get => _selectedSortIndex;
        set
        {
            if (_selectedSortIndex != value)
            {
                _selectedSortIndex = value;
                OnPropertyChanged();
                FiltrerEtTrier();
            }
        }
    }

    public DossierFactice? DossierSelectionne
    {
        get => _dossierSelectionne;
        set
        {
            if (_dossierSelectionne != value)
            {
                _dossierSelectionne = value;
                OnPropertyChanged();
            }
        }
    }

    public RelayCommand<DossierFactice> SupprimerDossierCommand { get; }

    public event PropertyChangedEventHandler? PropertyChanged;

    public HomeViewModel()
    {
        System.Diagnostics.Debug.WriteLine("HomeViewModel: Initialisation");
        
        Dossiers = new ObservableCollection<DossierFactice>();
        _dossiersFiltrés = new ObservableCollection<DossierFactice>();
        _suggestionsRecherche = new ObservableCollection<string>();
        
        SupprimerDossierCommand = new RelayCommand<DossierFactice>(dossier =>
        {
            if (dossier != null && Dossiers.Contains(dossier))
            {
                Dossiers.Remove(dossier);
                FiltrerEtTrier();
            }
        });

        // Charger les données au démarrage
        System.Diagnostics.Debug.WriteLine("HomeViewModel: Appel LoadDossiersAsync");
        _ = LoadDossiersAsync();
    }

    private async Task LoadDossiersAsync()
    {
        System.Diagnostics.Debug.WriteLine("LoadDossiersAsync: Démarrage");
        
        var dossiers = await _dossierService.GetAllDossiersAsync(new[] { 1, 2 });
        
        System.Diagnostics.Debug.WriteLine($"LoadDossiersAsync: {dossiers?.Count ?? 0} dossiers reçus");
        
        if (dossiers != null)
        {
            foreach (var dossier in dossiers)
            {
                Dossiers.Add(dossier);
                System.Diagnostics.Debug.WriteLine($"LoadDossiersAsync: Dossier ajouté - {dossier.NomEntreprise}");
            }
            
            FiltrerEtTrier();
        }
        else
        {
            System.Diagnostics.Debug.WriteLine("LoadDossiersAsync: Aucun dossier reçu");
        }
    }

    private void GénérerSuggestions()
    {
        SuggestionsRecherche.Clear();
        
        if (string.IsNullOrWhiteSpace(SearchText))
        {
            ShowSuggestions = false;
            return;
        }

        var searchLower = SearchText.ToLower();
        var suggestions = new HashSet<string>();

        // Générer les suggestions à partir de TOUS les dossiers
        foreach (var dossier in Dossiers)
        {
            if (dossier.NomEntreprise.ToLower().Contains(searchLower))
                suggestions.Add(dossier.NomEntreprise);
            
            if (dossier.Siret.ToLower().Contains(searchLower))
                suggestions.Add(dossier.Siret);
            
            if (dossier.PrenomNomContact.ToLower().Contains(searchLower))
                suggestions.Add(dossier.PrenomNomContact);
            
            if (dossier.EmailContact.ToLower().Contains(searchLower))
                suggestions.Add(dossier.EmailContact);
            
            if (dossier.Secteur.ToLower().Contains(searchLower))
                suggestions.Add(dossier.Secteur);
        }

        foreach (var suggestion in suggestions.OrderBy(s => s))
        {
            SuggestionsRecherche.Add(suggestion);
        }

        ShowSuggestions = SuggestionsRecherche.Count > 0;
    }

    private void FiltrerEtTrier()
    {
        var résultat = Dossiers.AsEnumerable();

        if (!string.IsNullOrWhiteSpace(SearchText))
        {
            var searchLower = SearchText.ToLower();
            résultat = résultat.Where(d => 
                d.NomEntreprise.ToLower().Contains(searchLower) ||
                d.Siret.ToLower().Contains(searchLower) ||
                d.PrenomNomContact.ToLower().Contains(searchLower) ||
                d.EmailContact.ToLower().Contains(searchLower) ||
                d.Secteur.ToLower().Contains(searchLower));
        }

        résultat = SelectedSortIndex switch
        {
            1 => résultat.OrderBy(d => d.NomEntreprise),
            2 => résultat.OrderByDescending(d => d.NomEntreprise),
            _ => résultat
        };

        DossiersFiltrés = new ObservableCollection<DossierFactice>(résultat.ToList());
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}
