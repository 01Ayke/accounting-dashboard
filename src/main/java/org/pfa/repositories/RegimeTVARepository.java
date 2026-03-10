package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.RegimeTVA;

public class RegimeTVARepository {
    public RegimeTVA find(String code) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        RegimeTVA regimeTVA = session.createSelectionQuery("FROM RegimeTVA WHERE code = :code", RegimeTVA.class)
                        .setParameter("code", code)
                        .getSingleResultOrNull();
        session.close();
        return regimeTVA;
    }
}
