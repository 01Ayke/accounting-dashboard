package org.pfa.models;

import java.util.Set;

public class SIG {
    private Set<Compte> comptes;

    private Double margeCommerciale = 0.0;
    private Double productionExercice = 0.0;
    private Double valeurAjoutee = 0.0;
    private Double excedentBrut = 0.0;
    private Double resultatExploitation = 0.0;
    private Double resultatFinancier = 0.0;
    private Double resultatExceptionnel = 0.0;
    private Double resultatExercice = 0.0;

    public SIG(Set<Compte> listeComptes) {
        this.comptes = listeComptes;
        this.margeCommerciale = computeMargeCommerciale();
        this.productionExercice = computeProductionExercice();
        this.valeurAjoutee = computeValeurAjoutee();
        this.excedentBrut = computeExcedentBrut();
        this.resultatExploitation = computeResultatExploitation();
        this.resultatFinancier = computeResultatFinancier();
        this.resultatExceptionnel = computeResultatExceptionnel();
        this.resultatExercice = computeResultatExercice();
    }

    private Boolean estComptabilise(Compte compte, String regex) {
        return (compte.getActif() && compte.getNumeroCompte().matches(regex));
    }

    private Double computeMargeCommerciale() {
        for (Compte compte: comptes) {
            if (estComptabilise(compte, "707.*"))
                margeCommerciale += compte.getSolde();
            else if (estComptabilise(compte, "607.*") || estComptabilise(compte, "6037.*"))
                margeCommerciale -= compte.getSolde();
         }
        return margeCommerciale;
    }

    private Double computeProductionExercice() {
        for (Compte compte: comptes) {
            if (estComptabilise(compte, "(70[1-6]|713|72).*"))
                productionExercice += compte.getSolde();
        }
        return productionExercice;
    }

    private Double computeValeurAjoutee() {
        valeurAjoutee += (margeCommerciale + productionExercice);
        for (Compte compte: comptes) {
            if (estComptabilise(compte, "(60(1|2)|603(1|2)|60[4-6]|61|62).*")) {
                valeurAjoutee -= compte.getSolde();
            }
        }
        return valeurAjoutee;
    }

    private Double computeExcedentBrut() {
        excedentBrut += valeurAjoutee;
        for (Compte compte: comptes) {
            if (estComptabilise(compte, "74.*"))
                excedentBrut += compte.getSolde();
            else if (estComptabilise(compte, "6(3|4(1|5)).*"))
                excedentBrut -= compte.getSolde();
        }
        return excedentBrut;
    }

    private Double computeResultatExploitation() {
        resultatExploitation += excedentBrut;
        for (Compte compte: comptes) {
            if (estComptabilise(compte, "7(5|81|91).*"))
                resultatExploitation += compte.getSolde();
            else if (estComptabilise(compte, "6(811|815|5).*"))
                resultatExploitation -= compte.getSolde();
        }
        return resultatExploitation;
    }

    private Double computeResultatFinancier() {
        for (Compte compte: comptes) {
            if (estComptabilise(compte, "76.*"))
                resultatFinancier += compte.getSolde();
            else if (estComptabilise(compte, "66.*"))
                resultatFinancier -= compte.getSolde();
        }
        return resultatFinancier;
    }

    private Double computeResultatExceptionnel() {
        for (Compte compte: comptes) {
            if (estComptabilise(compte, "77.*"))
                resultatExceptionnel += compte.getSolde();
            else if (estComptabilise(compte, "67.*"))
                resultatExceptionnel -= compte.getSolde();
        }
        return resultatExceptionnel;
    }

    private Double computeResultatExercice() {
        resultatExercice += (resultatExploitation + resultatFinancier - resultatExceptionnel);
        return resultatExercice;
    }

    public Double getMargeCommerciale() {
        return margeCommerciale;
    }

    public Double getProductionExercice() {
        return productionExercice;
    }

    public Double getValeurAjoutee() {
        return valeurAjoutee;
    }

    public Double getExcedentBrut() {
        return excedentBrut;
    }

    public Double getResultatExploitation() {
        return resultatExploitation;
    }

    public Double getResultatFinancier() {
        return resultatFinancier;
    }

    public Double getResultatExceptionnel() {
        return resultatExceptionnel;
    }

    public Double getResultatExercice() {
        return resultatExercice;
    }
}
