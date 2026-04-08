using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
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

    public bool IsAdmin => SessionManager.IsAdmin;

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

        System.Diagnostics.Debug.WriteLine("HomeViewModel: Appel RefreshAsync");
        _ = RefreshAsync();
    }

    public async Task RefreshAsync()
    {
        System.Diagnostics.Debug.WriteLine("RefreshAsync: Démarrage");

        var dossiers = await _dossierService.GetTousLesDossiersAsync();

        System.Diagnostics.Debug.WriteLine($"RefreshAsync: {dossiers?.Count ?? 0} dossiers reçus");

        if (dossiers != null)
        {
            Dossiers.Clear();
            foreach (var dossier in dossiers)
            {
                Dossiers.Add(dossier);
                System.Diagnostics.Debug.WriteLine($"RefreshAsync: Dossier ajouté - {dossier.DenominationSociale}");
            }

            FiltrerEtTrier();
        }
        else
        {
            System.Diagnostics.Debug.WriteLine("RefreshAsync: Aucun dossier reçu");
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

        foreach (var dossier in Dossiers)
        {
            if ((dossier.DenominationSociale ?? "").ToLower().Contains(searchLower))
                suggestions.Add(dossier.DenominationSociale ?? "");

            if ((dossier.PrenomNomContact ?? "").ToLower().Contains(searchLower))
                suggestions.Add(dossier.PrenomNomContact ?? "");

            if ((dossier.EmailContact ?? "").ToLower().Contains(searchLower))
                suggestions.Add(dossier.EmailContact ?? "");

            if ((dossier.Secteur ?? "").ToLower().Contains(searchLower))
                suggestions.Add(dossier.Secteur ?? "");
        }

        foreach (var suggestion in suggestions.Where(s => !string.IsNullOrWhiteSpace(s)).OrderBy(s => s))
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
                (d.DenominationSociale ?? "").ToLower().Contains(searchLower) ||
                (d.PrenomNomContact ?? "").ToLower().Contains(searchLower) ||
                (d.EmailContact ?? "").ToLower().Contains(searchLower) ||
                (d.Secteur ?? "").ToLower().Contains(searchLower));
        }

        résultat = SelectedSortIndex switch
        {
            1 => résultat.OrderBy(d => d.DenominationSociale),
            2 => résultat.OrderByDescending(d => d.DenominationSociale),
            _ => résultat
        };

        DossiersFiltrés = new ObservableCollection<DossierFactice>(résultat.ToList());
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
    {
        PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}
