package org.pfa.repositories;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.pfa.config.HibernateUtil;
import org.pfa.models.User;
import org.pfa.models.LOG.Log_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;
import org.pfa.models.Roles_enum;

public class UserRepository {

    private void initializeUser(User user) {
        if (user == null) return;
        Hibernate.initialize(user.getRoles());
    }

    public List<User> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> users = session.createSelectionQuery("FROM User", User.class).list();
        for (User user: users) {
            initializeUser(user);
        }
        session.close();
        Log.log("All users retrieved: " + users, Log_Type.INFO, Log_Location.Repository);
        return users;
    }

    public User findByMail(String mail) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = session.createSelectionQuery("FROM User WHERE mail = :mail", User.class)
                .setParameter("mail", mail)
                .getSingleResultOrNull();
        initializeUser(user);
        session.close();
        Log.log("User retrieved for mail " + mail + ": " + user, Log_Type.INFO, Log_Location.Repository);
        return user;
    }

    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        session.close();
        Log.log("User saved: " + user, Log_Type.INFO, Log_Location.Repository);
        return;
    }

    public User findById(Integer id) {
        try (org.hibernate.Session session = org.pfa.config.HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(org.pfa.models.User.class, id);
            Log.log("User retrieved for ID " + id + ": " + user, Log_Type.INFO, Log_Location.Repository);
            initializeUser(user);
            return user;
        }
        catch( Exception e) {
            Log.log("Error retrieving user for ID " + id + ": " + e.getMessage(), Log_Type.ERROR, Log_Location.Repository);
            return null;
        }
    }

    public List<Roles_enum> findRolesByID(Integer id_user) {
        try (org.hibernate.Session session = org.pfa.config.HibernateUtil.getSessionFactory().openSession()) {
            List<String> roleCodes = session.createNativeQuery(
                    "SELECT r.code FROM utilisateur_role ur " +
                    "JOIN role_utilisateur r ON ur.id_role = r.id_role " +
                    "WHERE ur.id_utilisateur = :id", 
                    String.class)
                    .setParameter("id", id_user)
                    .list();

            Log.log("Roles retrieved for user " + id_user + ": " + roleCodes, Log_Type.INFO, Log_Location.Repository);

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
        catch( Exception e) {
            Log.log("Error retrieving roles for user " + id_user + ": " + e.getMessage(), Log_Type.ERROR, Log_Location.Repository);
            return List.of();
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
            Log.log("Role " + id_role + " changed for user " + id_user, Log_Type.INFO, Log_Location.Repository);
            return true;
        } catch (Exception e) {
            Log.log("Failed to change role for user " + id_user + ": " + e.getMessage(), Log_Type.ERROR, Log_Location.Repository);
            return false;
        }
    }

    public void deleteByMail(String mail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM User WHERE mail = :mail")
                    .setParameter("mail", mail)
                    .executeUpdate();
            session.getTransaction().commit();
            Log.log("User with mail " + mail + " deleted", Log_Type.INFO, Log_Location.Repository);
        }
        catch( Exception e) {
            Log.log("Error deleting user with mail " + mail + ": " + e.getMessage(), Log_Type.ERROR, Log_Location.Repository);
        }
    }
}
