package org.pfa.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.pfa.models.*;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure("hibernate.cfg.xml") // fichier XML à créer
                    .addAnnotatedClass(File.class)
                    .addAnnotatedClass(Journal.class)
                    .addAnnotatedClass(EcritureComptable.class)
                    .addAnnotatedClass(Representant.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Document.class)
                    .addAnnotatedClass(CategorieDocument.class)
                    .addAnnotatedClass(TypeDocument.class)
                    .addAnnotatedClass(DocumentStatus.class)
                    .addAnnotatedClass(FormeJuridique.class)
                    .addAnnotatedClass(Address.class)
                    .addAnnotatedClass(Compte.class)
                    .addAnnotatedClass(RegimeFiscal.class)
                    .addAnnotatedClass(RegimeTVA.class)
                    .addAnnotatedClass(CompteAuxiliaire.class)
                    .addAnnotatedClass(TypeCompteAuxiliaire.class)
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
