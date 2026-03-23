using System;

namespace App.Models;

public class Dossier
{
    public Representant Representant { get; set; } = new();

    public string FormeJuridique { get; set; } = "";
    public string RegimeTva { get; set; } = "";
    public string RegimeFiscal { get; set; } = "";

    public DateTime DateDerniereModification { get; private set; } = DateTime.Now;

    public void ModifierFormeJuridique(FormeJuridique nouvelleForme)
    {
        FormeJuridique = nouvelleForme.ToString();
        Toucher();
    }

    public void ModifierRegimes(RegimeTva tva, RegimeFiscal fiscal)
    {
        RegimeTva = tva.ToString();
        RegimeFiscal = fiscal.ToString();
        Toucher();
    }

    private void Toucher()
    {
        DateDerniereModification = DateTime.Now;
    }
}