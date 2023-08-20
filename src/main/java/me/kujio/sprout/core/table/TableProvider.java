package me.kujio.sprout.core.table;

import me.kujio.sprout.core.entity.Order;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.entity.Where;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class TableProvider implements ProviderMethodResolver {

    public static String list(@Param("schema") String schema, @Param("query") Query query) {
        TableSchema tableSchema = TableRecord.getTableSchema(schema);
        return new SQL() {{
            SELECT(tableSchema.getSelectSql(query.getFields()));
            FROM(tableSchema.getName());
            WHERE(wheresQuerySql(tableSchema.getName(), query));
            ORDER_BY(ordersSql(tableSchema.getName(), query.getOrders()));
            if (query.getPage() != null) {
                LIMIT(query.getPage().getSize());
                OFFSET(query.getPage().getOffset());
            }
        }}.toString();
    }

    public static String count(@Param("schema") String schema, @Param("wheres") List<Where> wheres) {
        TableSchema tableSchema = TableRecord.getTableSchema(schema);
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM(tableSchema.getName());
            WHERE(wheresSql(tableSchema.getName(), wheres));
        }}.toString();
    }


    public static String get(@Param("schema") String schema, @Param("field") String field, @Param("value") Object value) {
        TableSchema tableSchema = TableRecord.getTableSchema(schema);
        return new SQL() {{
            SELECT(tableSchema.getSelectSql(null));
            FROM(tableSchema.getName());
            WHERE(tableSchema.getFieldWhereSql(field));
            LIMIT(1);
        }}.toString();
    }

    public static String add(@Param("schema") String schema, @Param("entities") List<Object> entities) {
        TableSchema tableSchema = TableRecord.getTableSchema(schema);
        return new SQL() {{
            INSERT_INTO(tableSchema.getName());
            INTO_COLUMNS(tableSchema.getColumnsSql());
        }} + tableSchema.getValuesSql(entities);
    }

    public static String set(@Param("schema") String schema, @Param("entity") Object entity, @Param("fixFields") List<String> fixFields) {
        TableSchema tableSchema = TableRecord.getTableSchema(schema);
        return new SQL() {{
            UPDATE(tableSchema.getName());
            SET(tableSchema.getUpdateSql(entity, fixFields));
            WHERE(tableSchema.getUpdateWhereSql(entity));
        }}.toString();
    }

    public static String del(@Param("schema") String schema, @Param("wheres") List<Where> wheres) {
        TableSchema tableSchema = TableRecord.getTableSchema(schema);
        return new SQL() {{
            DELETE_FROM(tableSchema.getName());
            WHERE(wheresSql(tableSchema.getName(), wheres));
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
