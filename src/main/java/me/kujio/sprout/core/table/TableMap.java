package me.kujio.sprout.core.table;

import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.utils.TimeUtils;
import org.springframework.data.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class TableMap extends HashMap<String, Object> {

    public <T> T map(TableSchema tableSchema) {
        try {
            Object entity = tableSchema.getConstructor().newInstance();
            for (Entry<String, Object> entry : entrySet()) {
                String field = entry.getKey();
                TableColumn column = tableSchema.getColumn(field);
                if (column == null) continue;
                Object value = entry.getValue();
                mapField(entity, column, value);
            }
            return (T) entity;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * same:
     * setter type: java.lang.Integer,	        value type: java.lang.Integer
     * setter type: java.lang.Long,	            value type: java.lang.Long
     * setter type: java.math.BigDecimal,	    value type: java.math.BigDecimal
     * setter type: java.lang.String,	        value type: java.lang.String
     * setter type: java.time.LocalDateTime,	value type: java.time.LocalDateTime
     * diff:
     * setter type: java.lang.Boolean,	        value type: java.lang.Integer
     * setter type: java.time.LocalDate,	    value type: java.sql.Date
     * setter type: java.time.LocalTime,	    value type: java.sql.Time
     */
    private static void mapField(Object entity, TableColumn column, Object value) {
        if (value == null) return;
        Class<?> sType = column.type();
        Class<?> vType = value.getClass();
        if (vType == Integer.class && sType == Boolean.class) {
            value = (Integer) value > 0;
        } else if (vType == java.sql.Date.class && sType == LocalDate.class) {
            value = TimeUtils.date2Ld((java.sql.Date) value);
        } else if (vType == java.sql.Time.class && sType == LocalTime.class) {
            value = TimeUtils.time2Lt((java.sql.Time) value);
        }else if (sType != vType){
            throw new SysException("""
                    字段：%s.%s 映射失败，Setter类型：%s，Value类型：%s
                    """.formatted(entity.getClass().getName(),column.field(),sType.getName(),vType.getName()));
        }
        try {
            column.setterMethod().invoke(entity,value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SysException("""
                    字段：%s.%s 映射失败，Setter类型：%s，Value类型：%s
                    """.formatted(entity.getClass().getName(),column.field(),sType.getName(),vType.getName()),e);
        }
    }

}
