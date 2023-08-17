package me.kujio.sprout.core.table;


import me.kujio.sprout.core.exception.SysException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableSchema {
    private final String name;
    private final List<TableColumn> columns;
    private final Set<String> fields;
    private final String selectSql;
    private final String columnsSql;
    private final String valuesSql;

    public TableSchema(String name, List<TableColumn> columns) {
        this.name = name;
        this.columns = columns;
        Set<String> fields = new HashSet<>();
        StringBuilder selSb = new StringBuilder();
        StringBuilder colSb = new StringBuilder();
        StringBuilder valSb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn col = columns.get(i);
            fields.add(col.field());
            if (i != 0) {
                selSb.append(",");
                colSb.append(",");
                valSb.append(",");
            }
            selSb.append(name).append(".`").append(col.column()).append("` AS `").append(col.field()).append("`");
            colSb.append("`").append(col.column()).append("`");
            valSb.append("#{entity.").append(col.field()).append("}");
        }
        this.fields = fields;
        this.selectSql = selSb.toString();
        this.columnsSql = colSb.toString();
        this.valuesSql = valSb.toString();
    }

    public String getName() {
        return name;
    }

    public List<TableColumn> getColumns() {
        return columns;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public String getColumnsSql() {
        return columnsSql;
    }

    public String getValuesSql() {
        return valuesSql;
    }

    public String[] getValuesSql(int length){
        String[] values = new String[length];
        for (int i = 0; i < length; i++) {
            values[i] = valuesSql.replaceAll("entity\\.","entities[" + i + "]");
        }
        return values;
    }

    public String getUpdateSql(Object entity, String... fixFields) {
        Set<String> fixFieldSet = Set.of(fixFields);
        StringBuilder sb = new StringBuilder();
        try {
            for (TableColumn column : columns) {
                Object obj = column.getterMethod().invoke(entity);
                if (obj == null && !fixFieldSet.contains(column.field())) continue;
                if (!sb.isEmpty()) sb.append(",");
                sb.append("`").append(column.column()).append("`=#{entity.").append(column.field()).append("}");
            }
            return sb.toString();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new SysException(e.getMessage());
        }
    }

}
