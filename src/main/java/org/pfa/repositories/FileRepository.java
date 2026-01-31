package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.File;

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
        File file = session.get(File.class, id);
        session.close();
        return file;
    }
}
