package org.pfa.models;

import java.util.*;

public class GrandLivre {
    private Map<String, List<AccountingEntry>> bilanCompte = new HashMap<>();

    public GrandLivre(File file, Date debut, Date fin) {
        for (Account compte: file.getComptes()) {
            List<AccountingEntry> ecritures = new ArrayList<>();
            for (AccountingEntry ecriture: compte.getEcrituresComptables()) {
                if (ecriture.getDate_facture().after(debut) && ecriture.getDate_facture().before(fin)) {
                    ecritures.add(ecriture);
                }
            }
            bilanCompte.put(compte.getNumero_compte(), ecritures);
        }
    }

    public Map<String, List<AccountingEntry>> getBilanCompte() {
        return bilanCompte;
    }
}
