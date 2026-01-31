package org.pfa.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.pfa.models.File;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml") // fichier XML à créer
                    .addAnnotatedClass(File.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Erreur Hibernate : " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
