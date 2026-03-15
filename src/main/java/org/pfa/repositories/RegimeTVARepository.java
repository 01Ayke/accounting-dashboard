package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.RegimeTVA;

import java.util.List;

public class RegimeTVARepository {
    public RegimeTVA findByCode(String code) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        RegimeTVA regimeTVA = session.createSelectionQuery("FROM RegimeTVA WHERE code = :code", RegimeTVA.class)
                        .setParameter("code", code)
                        .getSingleResultOrNull();
        session.close();
        return regimeTVA;
    }

    public List<RegimeTVA> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<RegimeTVA> regimesTVA = session.createQuery("FROM RegimeTVA", RegimeTVA.class).list();
        session.close();
        return regimesTVA;
    }

    public RegimeTVA findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        RegimeTVA regimeTVA = session.find(RegimeTVA.class, id);

        session.close();
        return regimeTVA;
    }
}
