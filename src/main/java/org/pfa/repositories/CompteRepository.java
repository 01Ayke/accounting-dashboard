package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Compte;
import org.pfa.models.File;

public class CompteRepository {
    public void save(Compte compte) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(compte);
        tx.commit();
        session.close();
    }

    public Compte getByFileAndNumero(File dossier, String numero) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Compte compte = session.createSelectionQuery("from Compte WHERE file = :dossier AND numeroCompte = :numero", Compte.class)
                .setParameter("dossier", dossier)
                .setParameter("numero", numero)
                .getSingleResultOrNull();
        session.close();
        return compte;
    }

    public Compte findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Compte compte = session.find(Compte.class, id);
        session.close();
        return compte;
    }
}
