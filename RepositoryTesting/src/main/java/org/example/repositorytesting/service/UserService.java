package org.example.repositorytesting.service;

import org.example.repositorytesting.model.User;
import org.example.repositorytesting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User save(User item) {
        return repository.save(item);
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<User> findByName(String name) {
        return repository.findByName(name);
    }
}
