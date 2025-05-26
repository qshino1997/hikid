package com.example.service.impl;

import com.example.dao.UserDAO;
import com.example.entity.User;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    @Override
    public User findById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public User findByIdAndProfile(int id) {
        return userDAO.findByIdAndProfile(id);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public void saveOrUpdate(User user) {
        userDAO.saveOrUpdate(user);
    }

    @Override
    public void updatePassword(String userEmail, String encodedPassword) {
        User u = findByEmail(userEmail);
        u.setPassword(encodedPassword);
        userDAO.saveOrUpdate(u);
    }

    @Override
    public List<User> getAllEmployees() {
        return userDAO.getAllEmployees();
    }

    @Override
    public List<User> getAllCustomers() {
        return userDAO.getAllCustomers();
    }

    @Override
    public void deleteById(int id) {
        userDAO.deleteById(id);
    }
}
