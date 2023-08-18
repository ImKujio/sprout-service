package me.kujio.sprout.core.table;


import me.kujio.sprout.core.exception.SysException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableSchema {
    private final String name;
    private final String primaryKey;
    private final boolean increment;
    private final List<TableColumn> columns;
    private final HashMap<String, TableColumn> fieldsMap;
    private final String selectSql;
    private final String columnsSql;
    private final String valuesSql;

    public TableSchema(String name, String primaryKey, boolean increment, List<TableColumn> columns) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.increment = increment;
        this.columns = columns;
        HashMap<String, TableColumn> fieldsMap = new HashMap<>();
        StringBuilder selSb = new StringBuilder();
        StringBuilder colSb = new StringBuilder();
        StringBuilder valSb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn col = columns.get(i);
            fieldsMap.put(col.field(), col);
            if (i != 0) {
                selSb.append(",");
                colSb.append(",");
                valSb.append(",");
            }
            selSb.append(name).append(".`").append(col.column()).append("` AS `").append(col.field()).append("`");
            colSb.append("`").append(col.column()).append("`");
            valSb.append("#{entity.").append(col.field()).append("}");
        }
        this.fieldsMap = fieldsMap;
        this.selectSql = selSb.toString();
        this.columnsSql = colSb.toString();
        this.valuesSql = valSb.toString();
    }

    public boolean hasField(String field) {
        return fieldsMap.containsKey(field);
    }

    public String getName() {
        return name;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public boolean isIncrement() {
        return increment;
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

    public String getFieldWhereSql(String field) {
        if (!fieldsMap.containsKey(field)) throw new SysException("entity " + name + " is not exist field " + field);
        TableColumn col = fieldsMap.get(field);
        return name + ".`" + col.column() + "` = #{value}";
    }

    public String getAllSql(List<String> fields) {
        Set<String> mixFields = new HashSet<>(fields);
        mixFields.add(primaryKey);
        mixFields.removeAll(this.fieldsMap.keySet());
        StringBuilder sb = new StringBuilder();
        for (String mixField : mixFields) {
            if (!sb.isEmpty()) sb.append(",");
            TableColumn col = this.fieldsMap.get(mixField);
            sb.append(name).append(".`").append(col.column()).append("` AS `").append(col.field()).append("`");
        }
        return sb.toString();
    }

    public String[] getValuesSql(int length) {
        String[] values = new String[length];
        for (int i = 0; i < length; i++) {
            values[i] = valuesSql.replaceAll("entity\\.", "entities[" + i + "]");
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

    public Integer getPrimaryValue(Object entity){
        try {
            TableColumn column = fieldsMap.get(primaryKey);
            Object obj = column.getterMethod().invoke(entity);
            if (obj == null) return null;
            if (obj instanceof Integer) return (Integer) obj;
            throw new SysException("primary value is not Integer type");
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new SysException("can not get primary value");
        }
    }

}
