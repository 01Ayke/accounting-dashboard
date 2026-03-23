package org.pfa.repositories;

import org.pfa.config.HibernateUtil;
import org.hibernate.Session;
import org.pfa.models.Roles;
import org.pfa.models.LOG.LOG_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;


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

}
