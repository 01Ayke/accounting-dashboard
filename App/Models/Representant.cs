using System;

namespace App.Models;

public class Representant
{
    public string NomEntreprise { get; set; } = string.Empty;
    public string Siret { get; set; } = string.Empty;

    public string Adresse { get; set; } = string.Empty;
    public string EmailContact { get; set; } = string.Empty;

    public DateTime DateDebutClient { get; set; } = DateTime.Now;
}