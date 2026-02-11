package org.pfa.repositories;
import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.TypeDocument;
import java.util.List;

public class TypeDocumentRepository {
    public TypeDocument findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(TypeDocument.class, id);
        }
    }
    // pour afficher la liste des types dans un menu déroulant
    public List<TypeDocument> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from TypeDocument", TypeDocument.class).list();
        }
    }
}