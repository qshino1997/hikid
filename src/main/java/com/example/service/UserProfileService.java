package com.example.service;

import com.example.entity.UserProfile;

public interface UserProfileService {
    UserProfile getByUserId(Integer userId);
    void saveOrUpdate(UserProfile profile);
}