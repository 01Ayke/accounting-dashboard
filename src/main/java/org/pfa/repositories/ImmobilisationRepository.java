package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Immobilisation;

public class ImmobilisationRepository {
    public void save(Immobilisation immobilisation) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(immobilisation);
        tx.commit();
        session.close();
    }
}
