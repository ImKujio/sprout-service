package me.kujio.sprout.core.mapper;

import me.kujio.sprout.core.query.Query;
import me.kujio.sprout.core.query.Where;
import me.kujio.sprout.core.table.TableProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TableMapper<T> {

    @SelectProvider(value = TableProvider.class, method = "list")
    List<T> list(@Param("entity") String entity, @Param("query") Query query);

    @SelectProvider(value = TableProvider.class, method = "count")
    int count(@Param("entity") String entity, @Param("wheres") List<Where> wheres);

    @SelectProvider(value = TableProvider.class, method = "get")
    T get(@Param("entity") String entity, @Param("field") String field, @Param("value") Object value);

    @InsertProvider(value = TableProvider.class, method = "add")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(@Param("entities") Object... entities);

    @UpdateProvider(value = TableProvider.class, method = "set")
    void set(@Param("entity") Object entity, @Param("fixFields") String... fixFields);

    @DeleteProvider(value = TableProvider.class, method = "del")
    void del(@Param("entity") String entity, @Param("wheres") List<Where> wheres);

}
