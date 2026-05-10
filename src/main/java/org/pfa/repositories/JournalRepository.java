package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Journal;
import java.util.List;

public class JournalRepository {
    public void save(Journal journal) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(journal);
                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace();
                throw e;
            }
        }
    }

    public Journal findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Journal journal = session.find(Journal.class, id);
        session.close();
        return journal;
    }

    public List<Journal> findByDossierId(Integer dossierId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Journal> journaux = session.createSelectionQuery(
                "FROM Journal j WHERE j.file.idDossier = :id", Journal.class)
                .setParameter("id", dossierId)
                .getResultList();
        session.close();
        return journaux;
    }
}
