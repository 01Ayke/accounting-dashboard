package org.pfa.models;

import java.util.*;

public class GrandLivre {
    private Map<String, List<LigneGrandLivre>> bilanCompte = new HashMap<>();

    public GrandLivre(File file, Date debut, Date fin) {
        for (Account compte: file.getComptes()) {
            List<LigneGrandLivre> lignes = new ArrayList<>();
            Double solde = 0.0;
            for (AccountingEntry ecriture: compte.getEcrituresComptables()) {
                if (ecriture.getDate_facture().after(debut) && ecriture.getDate_facture().before(fin)) {
                    LigneGrandLivre ligne = new LigneGrandLivre(ecriture, solde);
                    solde = ligne.getSoldeProgressif();
                    lignes.add(ligne);
                }
            }
            bilanCompte.put(compte.getNumero_compte(), lignes);
        }
    }

    public Map<String, List<LigneGrandLivre>> getBilanCompte() {
        return bilanCompte;
    }
}
