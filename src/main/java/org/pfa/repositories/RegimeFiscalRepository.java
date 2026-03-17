package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.FormeJuridique;
import org.pfa.models.RegimeFiscal;

import java.util.List;

public class RegimeFiscalRepository {
    public RegimeFiscal findByCode(String code) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        RegimeFiscal regimeFiscal = session.createSelectionQuery("FROM RegimeFiscal where code = :code", RegimeFiscal.class)
                .setParameter("code", code)
                .getSingleResultOrNull();
        session.close();
        return regimeFiscal;
    }

    public List<RegimeFiscal> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<RegimeFiscal> regimesFiscaux = session.createQuery("FROM RegimeFiscal", RegimeFiscal.class).list();
        session.close();
        return regimesFiscaux;
    }

    public RegimeFiscal findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        RegimeFiscal regimeFiscal = session.find(RegimeFiscal.class, id);

        session.close();
        return regimeFiscal;
    }
}
