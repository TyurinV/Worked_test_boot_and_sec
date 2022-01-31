package com.example.boot.service;

import com.example.boot.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void add(User user);

    void remove(Long id);

    void edit(User user);

    User getById(Long id);
}
