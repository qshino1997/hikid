package com.example.dao;

import com.example.entity.UserProfile;

public interface UserProfileDAO {
    UserProfile findByUserId(Integer userId);
    void saveOrUpdate(UserProfile profile);
}
