package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.FormeJuridique;

import java.util.List;

public class FormeJuridiqueRepository {
    public FormeJuridique findByCode(String code) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        FormeJuridique formeJuridique = session.createSelectionQuery("FROM FormeJuridique WHERE code = :code", FormeJuridique.class)
                        .setParameter("code", code)
                        .getSingleResultOrNull();
        session.close();
        return formeJuridique;
    }

    public List<FormeJuridique> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<FormeJuridique> formeJuridiques = session.createQuery("FROM FormeJuridique", FormeJuridique.class).list();
        session.close();
        return formeJuridiques;
    }

    public FormeJuridique findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        FormeJuridique formeJuridique = session.find(FormeJuridique.class, id);

        session.close();
        return formeJuridique;
    }
}
