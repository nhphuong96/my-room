package com.myroom.database.repository;

import java.util.List;

public interface IObjectRepository<T> {
    long add(T entity);
    T find(long id);
    List<T> findAll();
    boolean delete(long id);
    boolean update(T entity);
}
