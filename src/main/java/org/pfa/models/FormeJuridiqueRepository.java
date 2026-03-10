package org.pfa.models;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.pfa.config.HibernateUtil;

public class FormeJuridiqueRepository {
    public FormeJuridique find(String code) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        FormeJuridique formeJuridique = session.createSelectionQuery("FROM FormeJuridique WHERE code = :code", FormeJuridique.class)
                        .setParameter("code", code)
                        .getSingleResultOrNull();
        session.close();
        return formeJuridique;
    }
}
