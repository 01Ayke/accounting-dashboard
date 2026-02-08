package org.pfa.repositories;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.User;

public class UserRepository {
    public User findByMail(String mail, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = session.createSelectionQuery("FROM User WHERE mail = :mail AND mot_de_passe = :password", User.class)
                .setParameter("mail", mail)
                .setParameter("password", password)
                .getSingleResultOrNull();
        session.close();
        return user;
    }
}
