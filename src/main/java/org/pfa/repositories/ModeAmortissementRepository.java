package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.ModeAmortissement;

import java.util.List;

public class ModeAmortissementRepository {
    public List<ModeAmortissement> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ModeAmortissement> modesAmortissement = session.createQuery("FROM ModeAmortissement ", ModeAmortissement.class).list();
        session.close();
        return modesAmortissement;
    }
}
