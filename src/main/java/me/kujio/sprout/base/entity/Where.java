package me.kujio.sprout.base.entity;

import lombok.Getter;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Set;

@Getter
public class Where {
    private String column;
    private String type;
    private Object[] values;

    private Where() {
    }

    public Object getValue() {
        return values[0];
    }

    public static Where create(Logger log, EntityHandle<?> entityHandle, String field, String type, Object... values) {
        if (field.isBlank() || values == null || values.length == 0 || !entityHandle.containsKey(field)) {
            log.error("where条件错误：{} {} {}", field, type, values);
            return null;
        }
        Where where = new Where();
        switch (type) {
            case "=", ">", "<", ">=", "<=", "LIKE" -> {
                where.column = Field.camel2Snake(field);
                where.type = type.toUpperCase();
                where.values = values;
                return where;
            }
            case "IN" -> {
                if (values.length < 2) {
                    log.error("where条件错误：{} {} {}", field, type, values);
                    return null;
                }
                where.column = Field.camel2Snake(field);
                where.type = type.toUpperCase();
                where.values = values;
                return where;
            }
            case "BETWEEN" -> {
                if (values.length < 2) {
                    log.error("where条件错误：{} {} {}", field, type, values);
                    return null;
                }
                where.column = Field.camel2Snake(field);
                where.type = type.toUpperCase();
                where.values = Arrays.copyOfRange(values, 0, 2);
                return where;
            }
            default -> {
                log.error("where条件错误：{} {} {}", field, type, values);
                return null;
            }
        }
    }

    public static Where create(EntityHandle<?> entityHandle, String params, Set<String> removedFields) {
        if (params == null || params.isBlank()) return null;
        String[] split = params.split("\\|");
        if (split.length < 3 || !entityHandle.containsKey(split[0])) return null;
        if (removedFields != null && removedFields.contains(split[0])) return null;
        String column = Field.camel2Snake(split[0]);
        String type = split[1].toUpperCase();
        Where where = new Where();
        switch (type) {
            case "=":
            case ">":
            case "<":
            case ">=":
            case "<=":
            case "LIKE":
                where.column = column;
                where.type = type;
                where.values = new String[]{split[2]};
                return where;
            case "IN":
                if (split.length < 4) return null;
                where.column = column;
                where.type = type;
                where.values = Arrays.copyOfRange(split, 2, split.length);
                return where;
            case "BETWEEN":
                if (split.length < 4) return null;
                where.column = column;
                where.type = type;
                where.values = Arrays.copyOfRange(split, 2, 4);
                return where;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return '{' +
                "column=" + column +
                ", type=" + type +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
