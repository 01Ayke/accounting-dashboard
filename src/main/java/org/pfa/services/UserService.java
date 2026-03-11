package org.pfa.services;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.pfa.models.User;
import org.pfa.models.dto.RegisterRequest;
import org.pfa.repositories.UserRepository;
import org.pfa.models.Roles_enum;

public class UserService {
    private final UserRepository repo = new UserRepository();

    public User getOne(String mail, String password) {
        User user = repo.findByMail(mail);
        if (user != null && BCrypt.checkpw(password, user.getMot_de_passe())) {
            return user;
        }
        return null; // Invalid credentials
    }

    public List<Roles_enum> getRoles(Integer userId) {
        return repo.findRolesByID(userId);
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public boolean changeRole(Integer userId, List<Roles_enum> newRoles) {
        RoleService roleService = new RoleService();
        for (Roles_enum role : newRoles) {
            Integer roleId = roleService.getRoleIdByCode(role);
            if (roleId == null) {
                return false; // Role not found
            }
            if(!repo.changeRoleByID(userId, roleId)){
                return false; // Failed to change role
            }
        }
        return true;
    }

    public boolean register(RegisterRequest request) {
        if (repo.findByMail(request.getMail()) != null) {
            return false; // Email already in use
        }
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        User newUser = new User(request.getMail(), hashedPassword, request.getNom(), request.getPrenom(), request.getCode(), false);
        repo.save(newUser);
        return true;
    }
}
