namespace App.Models;

public class CreateDossierRequest
{
    public string DenominationSociale { get; set; } = "";
    public string Siret { get; set; } = "";
    public string FormeJuridique { get; set; } = "";
    public string RegimeFiscal { get; set; } = "";

    public string Prenom { get; set; } = "";
    public string Nom { get; set; } = "";
    public string Mail { get; set; } = "";
}