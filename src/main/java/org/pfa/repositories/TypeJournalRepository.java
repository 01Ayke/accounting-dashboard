package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.TypeJournal;

import java.util.List;

public class TypeJournalRepository {
    public List<TypeJournal> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<TypeJournal> typesJournal = session.createQuery("FROM TypeJournal", TypeJournal.class).list();
        session.close();
        return typesJournal;
    }

    public TypeJournal findById(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        TypeJournal typeJournal = session.find(TypeJournal.class, id);

        session.close();
        return typeJournal;
    }
}
