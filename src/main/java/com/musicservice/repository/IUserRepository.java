package com.musicservice.repository;

import com.musicservice.model.User;

import java.util.Map;

public interface IUserRepository {

    void save(User user);

    void update(User user);

    void delete(int id);

    User findById(int id);

    User findByEmail(String email);

    Map<Integer, User> findAll();

}