package com.example.dao.impl;

import com.example.dao.BaseDAO;
import com.example.dao.UserProfileDAO;
import com.example.entity.UserProfile;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileDAOImpl extends BaseDAO implements UserProfileDAO {
    @Override
    public UserProfile findByUserId(Integer userId) {
        UserProfile profile = currentSession().get(UserProfile.class, userId);
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser_id(userId);
        }
        return profile;
    }

    @Override
    public void saveOrUpdate(UserProfile profile) {
        currentSession().saveOrUpdate(profile);
    }
}
