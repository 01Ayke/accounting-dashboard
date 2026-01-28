package org.pfa.services;

import org.pfa.models.User;
import org.pfa.repositories.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository repo = new UserRepository();

    public List<User> getAll() { return repo.findAll(); }
    public User getOne(Long id) { return repo.findById(id); }
    public void create(User user) { repo.save(user); }
    public void update(User user) { repo.update(user); }
    public void delete(Long id) { repo.delete(id); }
}
