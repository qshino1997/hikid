package com.example.service.impl;

import com.example.entity.Role;
import com.example.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {
    @Override
    public Role findById(int id) {
        return null;
    }
}
