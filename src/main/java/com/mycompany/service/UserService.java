package com.mycompany.service;

import com.mycompany.model.User;

import java.util.List;

public interface UserService {

    User findByName(String name);
    User findById(Long id);
    List<User> findAll();
    void createUser(User user);
    void removeUser(Long id);
    void updateUser(User user);
}
