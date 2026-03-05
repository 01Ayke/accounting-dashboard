package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.RegimeFiscal;

public class RegimeFiscalRepository {
    public RegimeFiscal find(String code) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        RegimeFiscal regimeFiscal = session.createSelectionQuery("FROM RegimeFiscal where code = :code", RegimeFiscal.class)
                .setParameter("code", code)
                .getSingleResultOrNull();
        session.close();
        return regimeFiscal;
    }
}
