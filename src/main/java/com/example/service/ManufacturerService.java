package com.example.service;

import com.example.entity.Manufacturer;

import java.util.List;

public interface ManufacturerService {
    List<Manufacturer> getAll();
    Manufacturer findById(int id);
    List<Manufacturer> getPaged(int page, int size, String keyword);
    long count(String keyword);
    void saveOrUpdate(Manufacturer manufacturer);
    void deleteById(Integer id);
    Manufacturer findByName(String name);
}
