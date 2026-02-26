namespace App.Models;

/// <summary>
/// Représente les Soldes et Indicateurs de Gestion reçus du backend
/// </summary>
public class SIGBackend
{
    public double MargeCommerciale { get; set; }
    public double ProductionExercice { get; set; }
    public double ValeurAjoutee { get; set; }
    public double ExcedentBrut { get; set; }
    public double ResultatExploitation { get; set; }
    public double ResultatFinancier { get; set; }
    public double ResultatExceptionnel { get; set; }
    public double ResultatExercice { get; set; }
}
