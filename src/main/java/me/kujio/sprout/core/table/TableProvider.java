package me.kujio.sprout.core.table;

import me.kujio.sprout.base.entity.Query;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

public class TableProvider implements ProviderMethodResolver {

    public static String list(String entityName, Query query) {
        TableSchema tableSchema = TableRecord.getTableSchema(entityName);
        return new SQL() {{
            SELECT(tableSchema.getSelectSql());
            FROM(tableSchema.getName());
            WHERE(new String[]{""});
            ORDER_BY(new String[]{""});
            LIMIT(20);
            OFFSET(0);
        }}.toString();
    }

    public static String add(Object entity){
        TableSchema tableSchema = TableRecord.getTableSchema(entity.getClass().getSimpleName());
        return new SQL(){{
            INSERT_INTO(tableSchema.getName());
            VALUES(tableSchema.getColumnsSql(), tableSchema.getValuesSql());
        }}.toString();
    }

    public static String add(Object... entities){
        TableSchema tableSchema = TableRecord.getTableSchema(entities[0].getClass().getSimpleName());
        return new SQL(){{
            INSERT_INTO(tableSchema.getName());
            INTO_COLUMNS(tableSchema.getColumnsSql());
            INTO_VALUES(tableSchema.getValuesSql(entities.length));
        }}.toString();
    }

    public static String set(Object entity,String... nullableProps){
        TableSchema tableSchema = TableRecord.getTableSchema(entity.getClass().getSimpleName());
        return new SQL(){{
            UPDATE(tableSchema.getName());
            SET(tableSchema.getUpdateSql(entity,nullableProps));
        }}.toString();
    }

    public static void main(String[] args) {
        System.out.println(add());
    }

}
