package com.example.service.impl;

import com.example.dao.ManufacturerDAO;
import com.example.entity.Manufacturer;
import com.example.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    private ManufacturerDAO manufacturerDAO;

    @Override
    public List<Manufacturer> getAll() {
        return manufacturerDAO.getAll();
    }

    @Override
    public Manufacturer findById(int id) {
        return manufacturerDAO.findById(id);
    }

    @Override
    public List<Manufacturer> getPaged(int page, int size, String keyword) {
        return manufacturerDAO.findAll(page, size, keyword);
    }

    @Override
    public long count(String keyword) {
        return manufacturerDAO.count(keyword);
    }

    @Override
    public void saveOrUpdate(Manufacturer manufacturer) {
        manufacturerDAO.saveOrUpdate(manufacturer);
    }

    @Override
    public void deleteById(Integer id) {
        manufacturerDAO.deleteById(id);
    }

    @Override
    public Manufacturer findByName(String name) {
        return manufacturerDAO.findByName(name);
    }
}
