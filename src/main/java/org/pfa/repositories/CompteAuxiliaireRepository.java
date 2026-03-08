package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Compte;
import org.pfa.models.CompteAuxiliaire;

public class CompteAuxiliaireRepository {
    public void save(CompteAuxiliaire compteAuxiliaire) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(compteAuxiliaire);
        tx.commit();
        session.close();
    }

    public CompteAuxiliaire getByCompteAndLibelle(Compte compte, String libelle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CompteAuxiliaire compteAuxiliaire = session.createSelectionQuery("FROM CompteAuxiliaire WHERE compte = :compte AND libelle = :libelle", CompteAuxiliaire.class)
                .setParameter("compte", compte)
                .setParameter("numero", libelle)
                .getSingleResultOrNull();
        session.close();
        return compteAuxiliaire;
    }
}
