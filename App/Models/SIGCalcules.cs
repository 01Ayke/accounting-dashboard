namespace App.Models;

/// <summary>
/// Contient tous les Soldes et Indicateurs de Gestion (SIG) calculés
/// </summary>
public class SIGCalcules
{
    /// <summary>
    /// Marge commerciale = CA - Achats de marchandises ± variation de stocks
    /// </summary>
    public decimal MargeCommerciale { get; set; }

    /// <summary>
    /// Valeur Ajoutée = Marge commerciale + Production de l'exercice - charges externes
    /// </summary>
    public decimal ValeurAjoutee { get; set; }

    /// <summary>
    /// Excédent Brut d'Exploitation (EBE) = VA - Charges de personnel - impôts + subventions
    /// </summary>
    public decimal EBE { get; set; }

    /// <summary>
    /// Résultat d'Exploitation = EBE - Dotation aux amortissements ± charges/produits d'exploitation
    /// </summary>
    public decimal ResultatExploitation { get; set; }

    /// <summary>
    /// Résultat Courant Avant Impôt (RCAI) = Résultat d'exploitation + produits financiers - charges financières
    /// </summary>
    public decimal RCAI { get; set; }

    /// <summary>
    /// Résultat Exceptionnel = Produits exceptionnels - charges exceptionnelles
    /// </summary>
    public decimal ResultatExceptionnel { get; set; }

    /// <summary>
    /// Résultat Net = RCAI + Résultat exceptionnel - Impôt sur les bénéfices
    /// </summary>
    public decimal ResultatNet { get; set; }

    /// <summary>
    /// Retourne les SIG sous forme de dictionnaire pour faciliter l'affichage
    /// </summary>
    public Dictionary<string, decimal> ToDictionary()
    {
        return new Dictionary<string, decimal>
        {
            { "Marge Commerciale", MargeCommerciale },
            { "Valeur Ajoutée", ValeurAjoutee },
            { "EBE", EBE },
            { "Résultat d'Exploitation", ResultatExploitation },
            { "RCAI", RCAI },
            { "Résultat Exceptionnel", ResultatExceptionnel },
            { "Résultat Net", ResultatNet }
        };
    }
}
