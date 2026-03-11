package org.pfa.repositories;

import java.util.List;

import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.User;
import org.pfa.models.Roles;

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

    public Roles findRolebyID(Integer id) {
        try (org.hibernate.Session session = org.pfa.config.HibernateUtil.getSessionFactory().openSession()) {
            String roleCode = session.createNativeQuery(
                    "SELECT r.code FROM utilisateur_role ur " +
                    "JOIN role_utilisateur r ON ur.id_role = r.id_role " +
                    "WHERE ur.id_utilisateur = :id LIMIT 1", 
                    String.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
            
            if (roleCode == null) {
                return Roles.USER; // Rôle par défaut
            }
            
            try {
                return Roles.valueOf(roleCode);
            } catch (IllegalArgumentException e) {
                return Roles.USER; // Rôle par défaut si le code ne correspond pas
            }
        }
    }

    public boolean changeRoleByID(Integer id, Roles newRole) {
        try (org.hibernate.Session session = org.pfa.config.HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNativeQuery(
                    "UPDATE utilisateur_role ur " +
                    "JOIN role_utilisateur r ON ur.id_role = r.id_role " +
                    "SET ur.id_role = (SELECT id_role FROM role_utilisateur WHERE code = :newRole) " +
                    "WHERE ur.id_utilisateur = :id")    
                    .setParameter("newRole", newRole.name())
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            return false;
        }   
    }
}
