package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Compte;
import org.pfa.models.EcritureComptable;
import org.pfa.models.Journal;
import org.pfa.models.dto.CreationEcritureBatchRequest;
import org.pfa.models.dto.LigneEcritureDTO;

import java.util.Date;

public class EcritureComptableRepository {

    public void createBatchWithSession(CreationEcritureBatchRequest request) throws Exception {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Journal journal = session.find(Journal.class, request.getIdJournal());
            if (journal == null) {
                throw new Exception("Journal introuvable (id: " + request.getIdJournal() + ")");
            }

            Date dateSaisie = request.getDateSaisie() != null ? request.getDateSaisie() : new Date();

            for (LigneEcritureDTO ligne : request.getLignes()) {
                Compte compte = session.find(Compte.class, ligne.getIdCompte());
                if (compte == null) {
                    throw new Exception("Compte introuvable (id: " + ligne.getIdCompte() + ")");
                }

                EcritureComptable ecriture = new EcritureComptable(
                        dateSaisie,
                        ligne.getDebit() != null ? ligne.getDebit() : 0.0,
                        ligne.getCredit() != null ? ligne.getCredit() : 0.0,
                        request.getLibelle(),
                        compte,
                        journal);

                session.persist(ecriture);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e;
        }
    }
}
