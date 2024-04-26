package com.claksion.app.frame;

import java.util.List;

public interface BaseRepository<K,V> {

    int insert(V v) throws Exception;
    int delete(K k) throws Exception;
    int update(V v) throws Exception;;
    V selectOne(K k) throws Exception;;
    List<V> select() throws Exception;;
}