package me.kujio.sprout.core.mapper;

import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.entity.Where;
import me.kujio.sprout.core.table.TableProvider;
import me.kujio.sprout.system.entity.SysUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface TableMapper {

    @SelectProvider(value = TableProvider.class, method = "list")
    List<Map<String,Object>> list(@Param("entity") String entity, @Param("query") Query query);

    @SelectProvider(value = TableProvider.class, method = "list")
    Map<Integer,Map<String,Object>> map(@Param("entity") String entity, @Param("query") Query query);

    @SelectProvider(value = TableProvider.class, method = "count")
    int count(@Param("entity") String entity, @Param("wheres") List<Where> wheres);

    @SelectProvider(value = TableProvider.class, method = "all")
    Map<Integer,Map<String,Object>> all(@Param("entity") String entity,@Param("fields") List<String> fields);

    @SelectProvider(value = TableProvider.class, method = "get")
    Map<String,Object> get(@Param("entity") String entity, @Param("field") String field, @Param("value") Object value);

    @InsertProvider(value = TableProvider.class, method = "add")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(@Param("entities") List<Object> entities);

    @UpdateProvider(value = TableProvider.class, method = "set")
    void set(@Param("entity") Object entity, @Param("fixFields") String... fixFields);

    @DeleteProvider(value = TableProvider.class, method = "del")
    void del(@Param("entity") String entity, @Param("wheres") List<Where> wheres);

    @SelectProvider(value = TableProvider.class, method = "exist")
    boolean exist(@Param("entity") String entity, @Param("field") String field, @Param("value") Object value);

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
