package org.pfa.repositories;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.Document;
import org.pfa.models.File;
import org.pfa.models.Journal;
import org.pfa.models.Representant;

import java.util.List;

public class FileRepository {
    public List<File> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<File> files = session.createQuery("from File", File.class).list();
        session.close();
        return files;
    }

    public File findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        File file = session.find(File.class, id);

        Hibernate.initialize(file.getJournaux());
        for (Journal journal: file.getJournaux()) {
            Hibernate.initialize(journal.getEcrituresComptables());
        }

        Hibernate.initialize(file.getDocuments());

        Hibernate.initialize(file.getRepresentants());
        session.close();
        return file;
    }
}
