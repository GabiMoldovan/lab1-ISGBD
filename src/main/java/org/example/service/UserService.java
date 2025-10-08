package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public void deleteAllUsers() {
        userRepository.deleteAllUsers();
    }

    public void saveListOfUsers(List<User> users) {
        userRepository.saveListOfUsers(users);
    }

    private static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> parts = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            parts.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return parts;
    }

    private void shutdownAndAwait(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(1, TimeUnit.HOURS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void parallelInsertUsers(List<User> users, int threadCount, int batchSize) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for(List<User> batch : partition(users, batchSize)) {
            executorService.submit(() -> saveListOfUsers(batch));
        }
        shutdownAndAwait(executorService);
    }
}
