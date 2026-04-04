using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Helpers;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class AssignDossierViewModel : INotifyPropertyChanged
{
    private readonly DossierService _dossierService = new DossierService();
    private readonly UserService _userService = new UserService();

    private string _dossierId = "";
    private string _userId = "";
    private string? _message;
    private bool _isSuccess;
    private UserItem? _selectedUser;
    private DossierFactice? _selectedDossier;

    public ObservableCollection<DossierFactice> Dossiers { get; } = new();
    public ObservableCollection<UserItem> Users { get; } = new();

    public DossierFactice? SelectedDossier
    {
        get => _selectedDossier;
        set
        {
            if (_selectedDossier != value)
            {
                _selectedDossier = value;
                OnPropertyChanged();
                DossierId = value?.IdDossier.ToString() ?? "";
            }
        }
    }

    public UserItem? SelectedUser
    {
        get => _selectedUser;
        set
        {
            if (_selectedUser != value)
            {
                _selectedUser = value;
                OnPropertyChanged();
                UserId = value?.Id.ToString() ?? "";
            }
        }
    }

    public string DossierId
    {
        get => _dossierId;
        set { if (_dossierId != value) { _dossierId = value; OnPropertyChanged(); } }
    }

    public string UserId
    {
        get => _userId;
        set { if (_userId != value) { _userId = value; OnPropertyChanged(); } }
    }

    public string? Message
    {
        get => _message;
        set { if (_message != value) { _message = value; OnPropertyChanged(); } }
    }

    public bool IsSuccess
    {
        get => _isSuccess;
        set { if (_isSuccess != value) { _isSuccess = value; OnPropertyChanged(); } }
    }

    public RelayCommand<object> AssignCommand { get; }
    public RelayCommand<object> RevokeCommand { get; }

    public event PropertyChangedEventHandler? PropertyChanged;

    public AssignDossierViewModel()
    {
        AssignCommand  = new RelayCommand<object>(_ => _ = AssignAsync());
        RevokeCommand  = new RelayCommand<object>(_ => _ = RevokeAsync());
        _ = LoadUsersAsync();
        _ = LoadDossiersAsync();
    }

    private async Task LoadDossiersAsync()
    {
        var dossiers = await _dossierService.GetTousLesDossiersAsync();
        Dossiers.Clear();
        foreach (var d in dossiers)
            Dossiers.Add(d);
    }

    private async Task LoadUsersAsync()
    {
        var users = await _userService.GetUsersAsync();
        Users.Clear();
        foreach (var u in users)
            Users.Add(u);
    }

    private async Task AssignAsync()
    {
        Message = null;
        IsSuccess = false;

        if (!int.TryParse(DossierId, out var dossierId))
        { Message = "ID dossier invalide (entier requis)."; return; }

        if (!int.TryParse(UserId, out var userId))
        { Message = "Veuillez sélectionner un utilisateur."; return; }

        var request = new AssignDossierRequest
        {
            IdUser    = userId,
            IdDossier = dossierId
        };

        var (success, msg) = await _dossierService.AssignDossierAsync(request);
        IsSuccess = success;
        Message = msg;
    }

    private async Task RevokeAsync()
    {
        Message = null;
        IsSuccess = false;

        if (!int.TryParse(DossierId, out var dossierId))
        { Message = "ID dossier invalide."; return; }

        if (!int.TryParse(UserId, out var userId))
        { Message = "Veuillez sélectionner un utilisateur."; return; }

        var request = new AssignDossierRequest
        {
            IdUser    = userId,
            IdDossier = dossierId
        };

        var (success, msg) = await _dossierService.RevokeDossierAsync(request);
        IsSuccess = success;
        Message = msg;
    }

    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
