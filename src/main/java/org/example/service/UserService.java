package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;

import java.util.List;

public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public int count() {
        List<User> users = userRepository.findAll();
        return users.size();
    }
}
