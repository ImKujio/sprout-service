package me.kujio.sprout.core.table;

import me.kujio.sprout.core.entity.Order;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.entity.Where;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class TableProvider implements ProviderMethodResolver {

    public static String list(@Param("entity") String entity, @Param("query") Query query) {
        TableSchema tableSchema = TableRecord.getTableSchema(entity);
        return new SQL() {{
            SELECT(tableSchema.getSelectSql());
            FROM(tableSchema.getName());
            WHERE(wheresQuerySql(tableSchema.getName(), query));
            ORDER_BY(ordersSql(tableSchema.getName(), query.getOrders()));
            if (query.getPage() != null) {
                LIMIT(query.getPage().getSize());
                OFFSET(query.getPage().getOffset());
            }
        }}.toString();
    }

    public static String count(@Param("entity") String entity, @Param("wheres") List<Where> wheres) {
        TableSchema tableSchema = TableRecord.getTableSchema(entity);
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM(tableSchema.getName());
            WHERE(wheresSql(tableSchema.getName(), wheres));
        }}.toString();
    }

    public static String all(@Param("entity") String entity,@Param("fields") List<String> fields){
        TableSchema tableSchema = TableRecord.getTableSchema(entity);
        return new SQL(){{
           SELECT(tableSchema.getAllSql(fields));
           FROM(tableSchema.getName());
        }}.toString();
    }

    public static String get(@Param("entity") String entity, @Param("field") String field, @Param("value") Object value) {
        TableSchema tableSchema = TableRecord.getTableSchema(entity);
        return new SQL() {{
            SELECT(tableSchema.getSelectSql());
            FROM(tableSchema.getName());
            WHERE(tableSchema.getFieldWhereSql(field));
            LIMIT(1);
        }}.toString();
    }

    public static String add(@Param("entities") List<Object> entities) {
        TableSchema tableSchema = TableRecord.getTableSchema(entities.get(0).getClass().getSimpleName());
        return new SQL() {{
            INSERT_INTO(tableSchema.getName());
            INTO_COLUMNS(tableSchema.getColumnsSql());
            INTO_VALUES(tableSchema.getValuesSql(entities.size()));
        }}.toString();
    }

    public static String set(@Param("entity") Object entity, @Param("fixFields") String... fixFields) {
        TableSchema tableSchema = TableRecord.getTableSchema(entity.getClass().getSimpleName());
        return new SQL() {{
            UPDATE(tableSchema.getName());
            SET(tableSchema.getUpdateSql(entity, fixFields));
        }}.toString();
    }

    public static String del(@Param("entity") String entity, @Param("wheres") List<Where> wheres) {
        TableSchema tableSchema = TableRecord.getTableSchema(entity);
        return new SQL() {{
            DELETE_FROM(tableSchema.getName());
            WHERE(wheresSql(tableSchema.getName(), wheres));
        }}.toString();
    }

    public static String exist(@Param("entity") String entity, @Param("field") String field, @Param("value") Object value){
        TableSchema tableSchema = TableRecord.getTableSchema(entity);
        return new SQL(){{
            SELECT("COUNT(*)");
            FROM(tableSchema.getName());
            WHERE(tableSchema.getFieldWhereSql(field));
        }}.toString();
    }

    private static String[] wheresQuerySql(String table, Query query) {
        List<Where> wheres = query.getWheres();
        String[] lines = new String[wheres.size()];
        for (int i = 0; i < wheres.size(); i++) {
            Where where = wheres.get(i);
            lines[i] = where.getQuerySql(table, i);
        }
        return lines;
    }

    private static String[] wheresSql(String table, List<Where> wheres) {
        String[] lines = new String[wheres.size()];
        for (int i = 0; i < wheres.size(); i++) {
            Where where = wheres.get(i);
            lines[i] = where.getSql(table, i);
        }
        return lines;
    }

    private static String[] ordersSql(String table, List<Order> orders) {
        String[] lines = new String[orders.size()];
        for (int i = 0; i < orders.size(); i++) {
            lines[i] = orders.get(i).getSql(table);
        }
        return lines;
    }

}
