namespace App.Models;

public class JournalDto
{
    public int    Id      { get; set; }
    public string Code    { get; set; } = "";
    public string Libelle { get; set; } = "";

    public string DisplayText => $"{Code} — {Libelle}";
}
