package org.pfa.repositories;

import org.pfa.config.HibernateUtil;
import org.hibernate.Session;
import org.pfa.models.Roles;
import org.pfa.models.LOG.LOG_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;
import java.util.List;
import java.util.stream.Collectors;

public class RoleRepository {

    public Integer getIDbyCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Roles role = session.createSelectionQuery("FROM Roles WHERE code = :code", Roles.class)
                    .setParameter("code", code)
                    .getSingleResultOrNull();
            Log.log("Role retrieved for code :" + code + ": " + role, Log_Type.INFO, LOG_Location.Repository);
            return role != null ? role.getIdRole() : null;
        }
        catch( Exception e) {
            Log.log("Error retrieving role for code :" + code + ": " + e.getMessage(), Log_Type.ERROR, LOG_Location.Repository);
            return null;
        }
    }

    public List<String> getAllRoles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Roles> roles = session.createSelectionQuery("SELECT r FROM Roles r", Roles.class).list();
            Log.log("All roles retrieved: " + roles, Log_Type.INFO, LOG_Location.Repository);
            return roles.stream().map(Roles::getCode).collect(Collectors.toList());
        }
        catch( Exception e) {
            Log.log("Error retrieving all roles: " + e.getMessage(), Log_Type.ERROR, LOG_Location.Repository);
            return null;
        }
    }

}
