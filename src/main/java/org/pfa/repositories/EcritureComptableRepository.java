package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.EcritureComptable;

import java.util.List;

public class EcritureComptableRepository {

    public void createBatch(List<EcritureComptable> ecritures) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            for (EcritureComptable ecriture : ecritures) {
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
