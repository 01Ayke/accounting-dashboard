package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Journal;

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
}
