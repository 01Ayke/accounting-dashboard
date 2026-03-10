package org.pfa.services;

import org.mindrot.jbcrypt.BCrypt;
import org.pfa.models.User;
import org.pfa.models.dto.RegisterRequest;
import org.pfa.repositories.UserRepository;

public class UserService {
    private final UserRepository repo = new UserRepository();

    public User getOne(String mail, String password) {
        User user = repo.findByMail(mail);
        if (user != null && BCrypt.checkpw(password, user.getMot_de_passe())) {
            return user;
        }
        return null; // Invalid credentials
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
