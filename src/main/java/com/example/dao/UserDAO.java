package com.example.dao;

import com.example.entity.User;

import java.util.List;

public interface UserDAO {
    User findByIdAndProfile(int id);
    User findByEmail(String email);
    void saveOrUpdate(User user);
    List<User> getAllEmployees();
    List<User> getAllCustomers();
    User findById(int id);
    void deleteById(int id);
}
