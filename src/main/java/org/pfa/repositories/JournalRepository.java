package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Journal;
import java.util.List;

public class JournalRepository {
    public void save(Journal journal) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(journal);
        tx.commit();
        session.close();
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
