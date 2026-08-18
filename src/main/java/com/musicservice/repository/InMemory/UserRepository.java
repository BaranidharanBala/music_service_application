package com.musicservice.repository.InMemory;

import com.musicservice.model.User;
import com.musicservice.repository.IUserRepository;

import java.util.HashMap;
import java.util.Map;

public class UserRepository implements IUserRepository {

    private static UserRepository instance;
    private final Map<Integer, User> USERS;
    private int nextId;

    private UserRepository() {
        USERS = new HashMap<>();
        nextId = 0;
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    @Override
    public void save(User user) {
        nextId++;
        user.setId(nextId);
        USERS.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        USERS.put(user.getId(), user);
    }

    @Override
    public void delete(int id) {
        USERS.remove(id);
    }

    @Override
    public User findById(int id) {
        return USERS.get(id);
    }

    @Override
    public User findByEmail(String email) {
        for (User user : USERS.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Map<Integer, User> findAll() {
        return USERS;
    }

}