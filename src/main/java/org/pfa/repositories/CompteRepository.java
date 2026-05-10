package org.pfa.repositories;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Compte;
import org.pfa.models.CompteAuxiliaire;
import org.pfa.models.File;
import org.pfa.models.dto.CompteLight;

import java.util.ArrayList;
import java.util.List;

public class CompteRepository {
    public void save(Compte compte) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(compte);
                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                e.printStackTrace();
                throw e;
            }
        }
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

    public List<Compte> getByFile(File dossier) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Compte> comptes = session.createSelectionQuery("from Compte WHERE file = :dossier", Compte.class)
                .setParameter("dossier", dossier)
                .getResultList();
        for (Compte compte: comptes) {
            Hibernate.initialize(compte.getEcrituresComptables());
            Hibernate.initialize(compte.getComptesAuxiliaires());
            for (CompteAuxiliaire aux: compte.getComptesAuxiliaires()) {
                Hibernate.initialize(aux.getTypeCompteAuxiliaire());
            }
        }
        session.close();
        return comptes;
    }

    public List<CompteLight> getByFileLight(File dossier) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        List<Compte> comptes = session.createSelectionQuery("from Compte WHERE file = :dossier", Compte.class)
//                .setParameter("dossier", dossier)
//                .getResultList();
//        List<CompteLight> comptesLight = new ArrayList<>();
//        for (Compte compte: comptes) {
//            comptesLight.add(new CompteLight(compte));
//        }
//        session.close();
//        return comptesLight;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Compte> comptes = session.createQuery("FROM Compte where file = :dossier", Compte.class)
                .setParameter("dossier", dossier)
                .getResultList();
        List<CompteLight> comptesLight = new ArrayList<>();
        for (Compte compte: comptes) {
            Hibernate.initialize(compte.getComptesAuxiliaires());
            comptesLight.add(new CompteLight(compte));
        }
        return comptesLight;
    }

    public Compte findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Compte compte = session.find(Compte.class, id);
        session.close();
        return compte;
    }

    public void delete(Compte compte) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.remove(compte);
        tx.commit();
        session.close();
    }
}
