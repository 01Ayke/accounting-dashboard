package org.pfa.repositories;

import org.pfa.config.HibernateUtil;
import org.hibernate.Session;
import org.pfa.models.Roles;

public class RoleRepository {

    public Integer getIDbyCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Roles role = session.createSelectionQuery("FROM Roles WHERE code = :code", Roles.class)
                    .setParameter("code", code)
                    .getSingleResultOrNull();
            return role != null ? role.getIdRole() : null;
        }
    }

}
