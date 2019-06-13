package com.myroom.database.dao;

import java.util.List;

public interface IObjectDAO<T> {
    long add(T entity);
    T find(long id);
    List<T> findAll();
    boolean delete(long id);
    boolean update(T entity);
}
