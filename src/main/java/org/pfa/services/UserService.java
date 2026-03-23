package org.pfa.services;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.pfa.models.User;
import org.pfa.models.LOG.LOG_Location;
import org.pfa.models.LOG.Log;
import org.pfa.models.LOG.Log_Type;
import org.pfa.models.dto.RegisterRequest;
import org.pfa.repositories.UserRepository;
import org.pfa.models.Roles_enum;

public class UserService {
    private final UserRepository repo = new UserRepository();

    public User getOne(String mail, String password) {
        User user = repo.findByMail(mail);
        if (user != null && BCrypt.checkpw(password, user.getMot_de_passe())) {
            Log.log("User " + mail + " authenticated successfully", Log_Type.INFO, LOG_Location.Service);
            return user;
        }
        Log.log("Invalid credentials for user " + mail, Log_Type.ERROR, LOG_Location.Service);
        return null; // Invalid credentials
    }

    public List<Roles_enum> getRoles(Integer userId) {
        Log.log("Retrieving roles for user " + userId, Log_Type.INFO, LOG_Location.Service);
        return repo.findRolesByID(userId);
    }

    public List<User> getAll() {
        Log.log("Retrieving all users", Log_Type.INFO, LOG_Location.Service);
        return repo.findAll();
    }

    public boolean changeRole(Integer userId, List<Roles_enum> newRoles) {
        RoleService roleService = new RoleService();
        for (Roles_enum role : newRoles) {
            Integer roleId = roleService.getRoleIdByCode(role);
            if (roleId == null) {
                Log.log("Role not found for " + role, Log_Type.ERROR, LOG_Location.Service);
                return false; // Role not found
            }
            if(!repo.changeRoleByID(userId, roleId)){
                Log.log("User " + userId + " already has role " + role, Log_Type.WARNING, LOG_Location.Service);
                //return false; // Failed to change role
            }
            Log.log("Role " + role + " changed for user " + userId, Log_Type.INFO, LOG_Location.Service);
        }
        Log.log("All roles changed for user " + userId, Log_Type.INFO, LOG_Location.Service);
        return true;
    }

    public boolean register(RegisterRequest request) {
        if (repo.findByMail(request.getMail()) != null) {
            Log.log("Email already in use", Log_Type.ERROR, LOG_Location.Service);
            return false; // Email already in use
        }
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        User newUser = new User(request.getMail(), hashedPassword, request.getNom(), request.getPrenom(), request.getCode(), false);
        repo.save(newUser);
        Log.log("User registered successfully", Log_Type.INFO, LOG_Location.Service);
        return true;
    }
}
