package org.pfa.repositories;

import java.util.List;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.User;
import org.pfa.models.Roles_enum;

public class UserRepository {

    public List<User> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> users = session.createSelectionQuery("FROM User", User.class).list();
        session.close();
        return users;
    }

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

    public List<Roles_enum> findRolesByID(Integer id) {
        try (org.hibernate.Session session = org.pfa.config.HibernateUtil.getSessionFactory().openSession()) {
            List<String> roleCodes = session.createNativeQuery(
                    "SELECT r.code FROM utilisateur_role ur " +
                    "JOIN role_utilisateur r ON ur.id_role = r.id_role " +
                    "WHERE ur.id_utilisateur = :id", 
                    String.class)
                    .setParameter("id", id)
                    .list();
            
            return roleCodes.stream()
                    .map(code -> {
                        try {
                            return Roles_enum.valueOf(code);
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    })
                    .filter(role -> role != null)
                    .toList();
        }
    }

    public boolean changeRoleByID(Integer id_user, Integer id_role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createNativeQuery(
                    "INSERT INTO utilisateur_role (id_utilisateur, id_role) VALUES (:id_user, :id_role)")
                .setParameter("id_user", id_user)
                .setParameter("id_role", id_role)
                .executeUpdate();

            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
