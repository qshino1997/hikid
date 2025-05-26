package com.example.service.impl;

import com.example.dao.UserProfileDAO;
import com.example.entity.UserProfile;
import com.example.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileDAO profileDAO;

    @Override
    public UserProfile getByUserId(Integer userId) {
        return profileDAO.findByUserId(userId);
    }

    @Override
    public void saveOrUpdate(UserProfile profile) {
        LocalDateTime now = LocalDateTime.now();
        if (profile.getCreated_at() == null) {
            profile.setCreated_at(now);
        }
        profile.setUpdated_at(now);
        profileDAO.saveOrUpdate(profile);
    }
}

