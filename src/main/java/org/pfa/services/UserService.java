package org.pfa.services;

import org.pfa.models.User;
import org.pfa.repositories.UserRepository;

public class UserService {
    private final UserRepository repo = new UserRepository();

    public User getOne(String mail, String password) {
        User user = repo.findByMail(mail, password);
        return user;
    }
}
