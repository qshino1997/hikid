package com.example.service;

import com.example.entity.User;

public interface UserService {
    User findByEmail(String email);
}
