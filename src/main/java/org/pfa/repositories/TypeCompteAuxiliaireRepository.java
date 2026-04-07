package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.RegimeTVA;
import org.pfa.models.TypeCompteAuxiliaire;

public class TypeCompteAuxiliaireRepository {
    public TypeCompteAuxiliaire find(String code) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        TypeCompteAuxiliaire typeCompteAuxiliaire = session.createSelectionQuery("FROM TypeCompteAuxiliaire WHERE code = :code", TypeCompteAuxiliaire.class)
                        .setParameter("code", code)
                        .getSingleResultOrNull();
        session.close();
        return typeCompteAuxiliaire;
    }

    public TypeCompteAuxiliaire getOne(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        TypeCompteAuxiliaire typeCompteAuxiliaire = session.find(TypeCompteAuxiliaire.class, id);
        session.close();
        return typeCompteAuxiliaire;
    }
}
