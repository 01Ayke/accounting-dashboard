package org.pfa.services;
import org.pfa.models.Roles_enum;
import org.pfa.repositories.RoleRepository;

public class RoleService {
    private RoleRepository repo = new RoleRepository();
    public Integer getRoleIdByCode(Roles_enum role) {
        return repo.getIDbyCode(role.toString());
    }
}
