package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.User;

public class UserRepository {
    public User findByMail(String mail) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = session.createSelectionQuery("FROM User WHERE mail = :mail", User.class)
                .setParameter("mail", mail)
                .getSingleResultOrNull();
        session.close();
        return user;
    }

    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        session.close();
    }
    public User findById(Integer id) {
        try (org.hibernate.Session session = org.pfa.config.HibernateUtil.getSessionFactory().openSession()) {
            return session.get(org.pfa.models.User.class, id);
        }
    }
}
