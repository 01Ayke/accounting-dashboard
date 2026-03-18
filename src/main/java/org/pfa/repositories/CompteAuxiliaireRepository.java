package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Compte;
import org.pfa.models.CompteAuxiliaire;

import java.util.List;

public class CompteAuxiliaireRepository {
    public void save(CompteAuxiliaire compteAuxiliaire) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(compteAuxiliaire);
        tx.commit();
        session.close();
    }

    public CompteAuxiliaire getByCompteAndLibelle(Compte compte, String numero) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CompteAuxiliaire compteAuxiliaire = session.createSelectionQuery("FROM CompteAuxiliaire WHERE compte = :compte AND numero = :numero", CompteAuxiliaire.class)
                .setParameter("compte", compte)
                .setParameter("numero", numero)
                .getSingleResultOrNull();
        session.close();
        return compteAuxiliaire;
    }

    public List<CompteAuxiliaire> getById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CompteAuxiliaire> comptesAuxiliaires = session.createSelectionQuery("FROM CompteAuxiliaire WHERE idCompteAuxiliaire = :id", CompteAuxiliaire.class)
                .setParameter("id", id)
                .list();
        session.close();
        return comptesAuxiliaires;
    }

    public void delete(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createMutationQuery("DELETE FROM CompteAuxiliaire WHERE idCompteAuxiliaire = :id")
                .setParameter("id", id)
                .executeUpdate();
        tx.commit();
        session.close();
    }
}
