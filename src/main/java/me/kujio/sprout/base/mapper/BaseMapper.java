package me.kujio.sprout.base.mapper;

import me.kujio.sprout.base.entity.*;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface BaseMapper {

    List<Map<String, Object>> list(
            @Param("table") String table,
            @Param("wheres") List<Where.Item> wheres,
            @Param("orders") List<Order.Item> orders,
            @Param("page") Page page);

    Map<String, Object> get(@Param("table") String table, @Param("id") Integer id);

    @MapKey("id")
    Map<Integer, Map<String, Object>> all(@Param("table") String table, @Param("fields") List<Field> fields);

    int exist(@Param("table") String table, @Param("id") Integer id);

    int count(@Param("table") String table, @Param("wheres") List<Where.Item> wheres);

    void add(@Param("table") String table, @Param("fields") List<Field> fields, @Param("addRst") AddRst addRst);

    void set(@Param("table") String table, @Param("fields") List<Field> fields, @Param("id") Integer id);

    void del(@Param("table") String table, @Param("wheres") List<Where.Item> wheres);


}
