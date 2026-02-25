package org.pfa.models;

import java.util.Set;

public class SIG {
    private Set<Account> comptes;

    private Double margeCommerciale = 0.0;
    private Double productionExercice = 0.0;
    private Double valeurAjoutee = 0.0;
    private Double excedentBrut = 0.0;
    private Double resultatExploitation = 0.0;
    private Double resultatFinancier = 0.0;
    private Double resultatExceptionnel = 0.0;
    private Double resultatExercice = 0.0;

    public SIG(Set<Account> listeComptes) {
        this.comptes = listeComptes;
    }

    private Boolean estComptabilise(Account compte, String regex) {
        return (compte.getActif() && compte.getNumero_compte().matches(regex));
    }

    public Double getMargeCommerciale() {
        for (Account compte: comptes) {
            if (estComptabilise(compte, "707.*"))
                margeCommerciale += compte.getSolde();
            else if (estComptabilise(compte, "607.*") || estComptabilise(compte, "6037.*"))
                margeCommerciale -= compte.getSolde();
         }
        return margeCommerciale;
    }

    public Double getProductionExercice() {
        for (Account compte: comptes) {
            if (estComptabilise(compte, "(70[1-6]|713|72).*"))
                productionExercice += compte.getSolde();
        }
        return productionExercice;
    }

    public Double getValeurAjoutee() {
        valeurAjoutee += (margeCommerciale + productionExercice);
        for (Account compte: comptes) {
            if (estComptabilise(compte, "(60(1|2)|603(1|2)|60[4-6]|61|62).*")) {
                valeurAjoutee -= compte.getSolde();
            }
        }
        return valeurAjoutee;
    }

    public Double getExcedentBrut() {
        excedentBrut += valeurAjoutee;
        for (Account compte: comptes) {
            if (estComptabilise(compte, "74.*"))
                excedentBrut += compte.getSolde();
            else if (estComptabilise(compte, "6(3|4(1|5)).*"))
                excedentBrut -= compte.getSolde();
        }
        return excedentBrut;
    }

    public Double getResultatExploitation() {
        resultatExploitation += excedentBrut;
        for (Account compte: comptes) {
            if (estComptabilise(compte, "7(5|81|91).*"))
                resultatExploitation += compte.getSolde();
            else if (estComptabilise(compte, "6(811|815|5).*"))
                resultatExploitation -= compte.getSolde();
        }
        return resultatExploitation;
    }

    public Double getResultatFinancier() {
        for (Account compte: comptes) {
            if (estComptabilise(compte, "76.*"))
                resultatFinancier += compte.getSolde();
            else if (estComptabilise(compte, "66.*"))
                resultatFinancier -= compte.getSolde();
        }
        return resultatFinancier;
    }

    public Double getResultatExceptionnel() {
        for (Account compte: comptes) {
            if (estComptabilise(compte, "77.*"))
                resultatExceptionnel += compte.getSolde();
            else if (estComptabilise(compte, "67.*"))
                resultatExceptionnel -= compte.getSolde();
        }
        return resultatExceptionnel;
    }

    public Double getResultatExercice() {
        resultatExercice += (resultatExploitation + resultatFinancier - resultatExceptionnel);
        return resultatExercice;
    }
}
