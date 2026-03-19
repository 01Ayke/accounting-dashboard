package org.pfa.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.AffectationDossier;

import java.util.List;

public class AffectationDossierRepository {

    public void save(AffectationDossier affectation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(affectation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(AffectationDossier affectation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(affectation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public AffectationDossier findActiveByUserAndFile(Integer userId, Integer fileId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM AffectationDossier a WHERE a.user.id_utilisateur = :userId AND a.file.idDossier = :fileId AND a.actif = true", AffectationDossier.class)
                    .setParameter("userId", userId)
                    .setParameter("fileId", fileId)
                    .uniqueResult();
        }
    }

    public List<AffectationDossier> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM AffectationDossier", AffectationDossier.class).list();
        }
    }
}
