namespace App.Models;

public class LigneGrandLivre
{
    public string NumeroCompte { get; set; } = "";
    public string Date { get; set; } = "";
    public string Libelle { get; set; } = "";
    public decimal Debit { get; set; }
    public decimal Credit { get; set; }
    public decimal SoldeProgressif { get; set; }
}
