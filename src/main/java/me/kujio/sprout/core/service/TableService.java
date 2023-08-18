package me.kujio.sprout.core.service;



import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.entity.Where;

import java.util.List;
import java.util.Map;

public interface TableService<T> {

    List<T> list(Query query);
    List<T> list(List<Where> wheres);

    Map<Integer,T> map(Query query);
    Map<Integer,T> map(List<Where> wheres);

    int count(Query query);
    int count(List<Where> wheres);

    Map<Integer,T> all(List<String> fields);

    T get(Integer pkValue);
    T get(String field,Object value);

    void add(T entity);
    void add(List<T> entities);

    void set(T entity);
    void set(T entity,String... fixFields);

    void put(T entity);
    void put(List<T> entities,String field,Object value);
    void put(List<T> entities,List<Where> wheres);

    void del(Integer pkValue);
    void del(List<Where> wheres);

    boolean exist(Integer pkValue);
    boolean exist(String field,Object value);

    String cacheKey(String... tags);

    void clearCache();
}
