package com.example.dao;

import com.example.entity.Manufacturer;

import java.util.List;

public interface ManufacturerDAO {
    List<Manufacturer> getAll();
    Manufacturer findById(int id);

    Manufacturer findByName(String name);

    List<Manufacturer> findAll(int page, int size, String keyword);
    long count(String keyword);
    void saveOrUpdate(Manufacturer manufacturer);
    void deleteById(Integer id);
}
