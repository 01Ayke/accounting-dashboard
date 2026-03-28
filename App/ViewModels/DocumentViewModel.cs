using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Threading.Tasks;
using App.Models;
using App.Services;

namespace App.ViewModels;

public class DocumentViewModel : INotifyPropertyChanged
{
    private readonly DocumentService _documentService = new DocumentService();

    private ObservableCollection<Document> _documents = new();
    private string? _message;

    public ObservableCollection<Document> Documents
    {
        get => _documents;
        set { if (_documents != value) { _documents = value; OnPropertyChanged(); } }
    }

    public string? Message
    {
        get => _message;
        set { if (_message != value) { _message = value; OnPropertyChanged(); } }
    }

    public DocumentViewModel()
    {
        _ = LoadAsync();
    }

    private async Task LoadAsync()
    {
        var docs = await _documentService.GetDocumentsAsync();
        Documents = new ObservableCollection<Document>(docs);
        if (Documents.Count == 0)
            Message = "Aucun document trouvé.";
    }

    public event PropertyChangedEventHandler? PropertyChanged;
    protected void OnPropertyChanged([CallerMemberName] string? name = null)
        => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
}
