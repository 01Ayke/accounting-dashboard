package org.pfa.repositories;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.pfa.config.HibernateUtil;
import org.pfa.models.*;
import org.pfa.models.dto.FileLight;

import java.util.ArrayList;
import java.util.List;

public class FileRepository {
    private void initializeFile(File file) {
        if (file == null) return;
        
        Hibernate.initialize(file.getJournaux());
        Hibernate.initialize(file.getUtilisateurs());
        for (Journal journal: file.getJournaux()) {
            Hibernate.initialize(journal.getEcrituresComptables());
        }
        for (Compte compte: file.getComptes()) {
            Hibernate.initialize(compte.getImmobilisations());
            Hibernate.initialize(compte.getEcrituresComptables());
            Hibernate.initialize(compte.getComptesAuxiliaires());
            for (EcritureComptable ecriture: compte.getEcrituresComptables()) {
                Hibernate.initialize(ecriture.getJournal());
            }
        }

        Hibernate.initialize(file.getDocuments());
        Hibernate.initialize((file.getComptes()));

        Hibernate.initialize(file.getRepresentants());
    }

    public List<File> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<File> files = session.createQuery("from File", File.class).list();
        for (File file: files) {
            initializeFile(file);
        }
        session.close();
        return files;
    }

    public List<FileLight> findUserFilesLight(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<AffectationDossier> affectations = session.createQuery("FROM AffectationDossier WHERE user = :user AND actif = true", AffectationDossier.class)
                .setParameter("user", user)
                .list();

        List<FileLight> filesLight = new ArrayList<>();
        for (AffectationDossier affectation : affectations) {
            File file = affectation.getFile();
            if (file != null) {
                Hibernate.initialize(file.getRepresentants());
                filesLight.add(new FileLight(file));
            }
        }
        session.close();
        return filesLight;
    }

    public List<FileLight> findAllLight() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<File> files = session.createQuery("from File", File.class).list();
        List<FileLight> filesLight = new ArrayList<>();
        for (File file: files) {
            Hibernate.initialize(file.getRepresentants());
            filesLight.add(new FileLight(file));
        }
        session.close();
        return filesLight;
    }

    public File findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        File file = session.find(File.class, id);
        
        if (file != null) {
            initializeFile(file);
        }
        
        session.close();
        return file;
    }

    public File findByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        File file = session.createSelectionQuery("FROM File where denominationSociale = :name", File.class)
                .setParameter("name", name)
                .getSingleResultOrNull();
        if (file != null) {
            initializeFile(file);
        }
        session.close();
        return file;
    }

    public void save(File file) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(file);
        tx.commit();
        session.close();
    }

    public void deleteByName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createMutationQuery("DELETE FROM File WHERE denominationSociale = :name")
                .setParameter("name", name)
                .executeUpdate();
        tx.commit();
        session.close();
    }
}
