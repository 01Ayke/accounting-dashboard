package org.pfa.repositories;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class FileRepository {
    private void initializeFile(File file) {
        if (file == null) return;
        
        Hibernate.initialize(file.getJournaux());
        for (Journal journal: file.getJournaux()) {
            Hibernate.initialize(journal.getEcrituresComptables());
        }
        for (Account compte: file.getComptes()) {
            Hibernate.initialize(compte.getEcrituresComptables());
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

    public File findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        File file = session.find(File.class, id);
        
        if (file != null) {
            initializeFile(file);
        }
        
        session.close();
        return file;
    }
}
