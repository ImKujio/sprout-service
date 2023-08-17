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

    public static String add(){
//        TableSchema tableSchema = TableRecord.getTableSchema(obj.getClass().getSimpleName());
        return new SQL(){{
            INSERT_INTO("tab");
            VALUES("a,b","#{entity.name},#{}");
        }}.toString();
    }

    public static void main(String[] args) {
        System.out.println(add());
    }

}
