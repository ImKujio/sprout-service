package me.kujio.sprout.core.mapper;

import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.entity.Where;
import me.kujio.sprout.core.table.TableMap;
import me.kujio.sprout.core.table.TableProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TableMapper {

    @SelectProvider(value = TableProvider.class, method = "list")
    List<TableMap> list(@Param("schema") String schema, @Param("query") Query query);

    @SelectProvider(value = TableProvider.class, method = "count")
    int count(@Param("schema") String schema, @Param("wheres") List<Where> wheres);

    @SelectProvider(value = TableProvider.class, method = "get")
    TableMap get(@Param("schema") String schema, @Param("field") String field, @Param("value") Object value);

    @InsertProvider(value = TableProvider.class, method = "add")
    @Options(useGeneratedKeys = true, keyProperty = "entities.id")
    void add(@Param("schema") String schema, @Param("entities") List<?> entities);

    @UpdateProvider(value = TableProvider.class, method = "set")
    void set(@Param("schema") String schema, @Param("entity") Object entity, @Param("fixFields") List<String> fixFields);

    @DeleteProvider(value = TableProvider.class, method = "del")
    void del(@Param("schema") String schema, @Param("wheres") List<Where> wheres);


    @Component
    class Holder {
        private static TableMapper tableMapper;

        public Holder(TableMapper tableMapper) {
            Holder.tableMapper = tableMapper;
        }

        public static TableMapper get() {
            return tableMapper;
        }
    }
}
