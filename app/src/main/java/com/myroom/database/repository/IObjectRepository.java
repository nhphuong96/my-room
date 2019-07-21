package com.myroom.database.repository;

import java.util.List;

public interface IObjectRepository<T> {
    long add(T entity);
    T find(long key);
    List<T> findAll();
    boolean delete(long key);
    boolean update(T entity);
}
