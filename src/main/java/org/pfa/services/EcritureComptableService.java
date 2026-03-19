package org.pfa.services;

import org.pfa.models.Compte;
import org.pfa.models.EcritureComptable;
import org.pfa.models.Journal;
import org.pfa.models.dto.CreationEcritureBatchRequest;
import org.pfa.models.dto.LigneEcritureDTO;
import org.pfa.repositories.CompteRepository;
import org.pfa.repositories.EcritureComptableRepository;
import org.pfa.repositories.JournalRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EcritureComptableService {
    private final EcritureComptableRepository repo = new EcritureComptableRepository();
    private final CompteRepository compteRepo = new CompteRepository();
    private final JournalRepository journalRepo = new JournalRepository();

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

        Journal journal = journalRepo.findById(request.getIdJournal());
        if (journal == null) {
            throw new Exception("Journal introuvable (id: " + request.getIdJournal() + ")");
        }

        Date dateSaisie = new Date();
        List<EcritureComptable> entitiesToSave = new ArrayList<>();

        for (LigneEcritureDTO ligne : request.getLignes()) {
            Compte compte = compteRepo.findById(ligne.getIdCompte());
            if (compte == null) {
                throw new Exception("Compte introuvable (id: " + ligne.getIdCompte() + ")");
            }

            EcritureComptable ecriture = new EcritureComptable(
                    dateSaisie,
                    ligne.getDebit() != null ? ligne.getDebit() : 0.0,
                    ligne.getCredit() != null ? ligne.getCredit() : 0.0,
                    request.getCodeOperation(),
                    request.getNumeroFacture(),
                    request.getLibelle(),
                    request.getDateFacture(),
                    compte,
                    journal);

            entitiesToSave.add(ecriture);
        }

        repo.createBatch(entitiesToSave);
    }
}
