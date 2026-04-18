package org.pfa.services;

import org.pfa.models.dto.CreationEcritureBatchRequest;
import org.pfa.models.dto.LigneEcritureDTO;
import org.pfa.repositories.EcritureComptableRepository;

public class EcritureComptableService {
    private final EcritureComptableRepository repo = new EcritureComptableRepository();

    public void createBatch(CreationEcritureBatchRequest request) throws Exception {
        if (request.getLignes() == null || request.getLignes().size() < 2) {
            throw new Exception("L'écriture doit contenir au moins deux lignes.");
        }

        double totalDebit = 0.0;
        double totalCredit = 0.0;

        for (LigneEcritureDTO ligne : request.getLignes()) {
            Double debit = ligne.getDebit() != null ? ligne.getDebit() : 0.0;
            Double credit = ligne.getCredit() != null ? ligne.getCredit() : 0.0;
            totalDebit += debit;
            totalCredit += credit;
        }

        if (Math.abs(totalDebit - totalCredit) > 0.01) {
            throw new Exception("Ecriture déséquilibrée : Débit (" + totalDebit + ") != Crédit (" + totalCredit + ")");
        }

        // Le repository prend le relais avec UNE SEULE session ouverte 
        // pour récupérer Journal, Compte et sauvegarder le tout de manière sécurisée.
        repo.createBatchWithSession(request);
    }
}
