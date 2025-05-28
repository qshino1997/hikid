package com.example.dao;

import com.example.entity.User;

import java.util.List;

public interface UserDAO {
    User findByIdAndProfile(int id);
    User findByEmail(String email);
    void saveOrUpdate(User user);
    List<User> getAllUsers(int page, int size, int role, String keyword);
    User findById(int id);
    void deleteById(int id);
    long countEmployees(int role, String keyword);

}
