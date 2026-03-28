using System;
using System.Collections.Generic;

namespace App.Models;

public class Dossier
{
    public Representant Representant { get; set; } = new();

    public FormeJuridique FormeJuridique { get; set; }
    public RegimeTva RegimeTva { get; set; }
    public RegimeFiscal RegimeFiscal { get; set; }

    public DateTime DateDerniereModification { get; private set; } = DateTime.Now;
    

    public void ModifierFormeJuridique(FormeJuridique nouvelleForme)
    {
        FormeJuridique = nouvelleForme;
        Toucher();
    }

    public void ModifierRegimes(RegimeTva tva, RegimeFiscal fiscal)
    {
        RegimeTva = tva;
        RegimeFiscal = fiscal;
        Toucher();
    }

    private void Toucher()
    {
        DateDerniereModification = DateTime.Now;
    }
}