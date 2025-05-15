package com.example.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDAO {
    @Autowired
    private SessionFactory sessionFactory;
    public Session currentSession() { return sessionFactory.getCurrentSession(); }
}
