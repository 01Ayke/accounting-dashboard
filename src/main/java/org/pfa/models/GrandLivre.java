package org.pfa.models;

import java.util.*;

public class GrandLivre {
    private Map<String, List<LigneGrandLivre>> bilanCompte = new HashMap<>();
    private final SortByDate sorter = new SortByDate();
    private String denominationSociale;

    public GrandLivre(File file, Date debut, Date fin) {
        this.denominationSociale = file.getDenominationSociale();
        for (Compte compte: file.getComptes()) {
            List<LigneGrandLivre> lignes = new ArrayList<>();
            Double solde = 0.0;
            for (EcritureComptable ecriture: compte.getEcrituresComptables()) {
                if (ecriture.getDate_facture().after(debut) && ecriture.getDate_facture().before(fin)) {
                    LigneGrandLivre ligne = new LigneGrandLivre(ecriture, solde);
                    solde = ligne.getSoldeProgressif();
                    lignes.add(ligne);
                }
            }
            lignes.sort(sorter);
            bilanCompte.put(compte.getNumero_compte(), lignes);
        }
    }

    public String getDenominationSociale() {
        return denominationSociale;
    }

    public Map<String, List<LigneGrandLivre>> getBilanCompte() {
        return bilanCompte;
    }
}
