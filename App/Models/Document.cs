namespace App.Models;

public class Document
{
    public string Nom { get; set; } = string.Empty;
    public string Chemin { get; set; } = string.Empty;
    public DateTime DateAjout { get; set; } = DateTime.Now;
}