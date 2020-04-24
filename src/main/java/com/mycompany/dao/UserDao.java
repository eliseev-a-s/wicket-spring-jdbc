package com.mycompany.dao;

import com.mycompany.model.User;

import java.util.List;

public interface UserDao {

    User findByName(String name);
    User findById(Long id);
    List<User> findAll();
    boolean createUser(User user);
    void removeUser(Long id);
    void updateUser(User user);
}
