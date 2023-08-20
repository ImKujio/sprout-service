package me.kujio.sprout.core.table;


import me.kujio.sprout.core.exception.SysException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private final Constructor<?> constructor;
    private final String selectSql;
    private final String columnsSql;
    private final String valuesSql;

    public TableSchema(String name, String primaryKey, boolean increment, List<TableColumn> columns, Constructor<?> constructor) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.increment = increment;
        this.columns = columns;
        this.constructor = constructor;
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

    public TableColumn getColumn(String field) {
        return fieldsMap.get(field);
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

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public String getSelectSql(List<String> fields) {
        if (fields == null || fields.isEmpty()) return selectSql;
        Set<String> mixFields = new HashSet<>(fields);
        mixFields.add(primaryKey);
        mixFields.retainAll(this.fieldsMap.keySet());
        StringBuilder sb = new StringBuilder();
        for (String mixField : mixFields) {
            if (!sb.isEmpty()) sb.append(",");
            TableColumn col = this.fieldsMap.get(mixField);
            sb.append(name).append(".`").append(col.column()).append("` AS `").append(col.field()).append("`");
        }
        return sb.toString();
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

    public String getValuesSql(List<Object> entities) {
        StringBuilder sb = new StringBuilder(" VALUES");
        for (int i = 0; i < entities.size(); i++) {
            Object entity = entities.get(i);
            sb.append(i == 0 ? " (" : ", (");
            try {
                for (int j = 0; j < columns.size(); j++) {
                    if (j != 0) sb.append(",");
                    TableColumn col = columns.get(j);
                    Object obj = col.getterMethod().invoke(entity);
                    if (obj != null) sb.append("#{entities[").append(i).append("].").append(col.field()).append("}");
                    else sb.append("DEFAULT");
                }
            }catch (InvocationTargetException | IllegalAccessException e){
                throw new SysException("create values sql failed!",e);
            }
            sb.append(")");
        }
        return sb.toString();
    }

    public String getUpdateSql(Object entity, List<String> fixFields) {
        Set<String> fixFieldSet = new HashSet<>(fixFields);
        StringBuilder sb = new StringBuilder();
        try {
            for (TableColumn column : columns) {
                if (column.field().equals(primaryKey)) continue;
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

    public String getUpdateWhereSql(Object entity) {
        Integer pkValue = getPrimaryValue(entity);
        if (pkValue == null) throw new SysException("primary key of update data is null");
        return name + ".`" + primaryKey + "` = #{entity." + primaryKey + "}";
    }

    public Integer getPrimaryValue(Object entity) {
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
