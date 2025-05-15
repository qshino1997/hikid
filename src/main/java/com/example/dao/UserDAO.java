package com.example.dao;

import com.example.entity.User;

public interface UserDAO {
    User findByEmail(String email);
}
