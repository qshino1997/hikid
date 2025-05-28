package com.example.service;

import com.example.entity.User;
import com.example.entity.UserProfile;

import java.util.List;

public interface UserService {
    User findById(int id);
    User findByIdAndProfile(int id);
    User findByEmail(String email);
    void saveOrUpdate(User user);
    void updatePassword(String email, String encodedPassword);
    List<User> getAllUsers(int page, int size, int role, String keyword);
    void deleteById(int id);
    long countEmployees(int role, String keyword);

}
