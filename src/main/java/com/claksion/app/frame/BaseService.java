package com.claksion.app.frame;


import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BaseService<K, V> {
    @Transactional
    int add(V v) throws Exception;
    @Transactional
    int del(K k) throws Exception;
    @Transactional
    int modify(V v) throws Exception;
    @Transactional
    V get(K k) throws Exception;
    @Transactional
    List<V> get() throws Exception;
}