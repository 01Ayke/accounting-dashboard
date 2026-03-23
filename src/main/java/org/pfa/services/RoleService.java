package org.pfa.services;
import org.pfa.models.Roles_enum;
import org.pfa.models.LOG.LOG_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;
import org.pfa.repositories.RoleRepository;

public class RoleService {
    private RoleRepository repo = new RoleRepository();
    public Integer getRoleIdByCode(Roles_enum role) {
        Log.log("Getting role ID for role " + role, Log_Type.INFO, LOG_Location.Service);
        return repo.getIDbyCode(role.toString());
    }
}
