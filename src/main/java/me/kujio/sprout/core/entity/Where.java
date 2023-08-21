package me.kujio.sprout.core.entity;

import lombok.Getter;
import lombok.NonNull;
import me.kujio.sprout.core.exception.SysException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
public final class Where {
    private final String field;
    private final String type;
    private final Object[] values;
    private final boolean ignored;

    public static final String SEPARATOR = "┆";
    private static final Set<String> TYPES = Set.of("=", "<", "<=", ">", ">=", "LIKE", "BETWEEN", "IN");

    private Where(boolean ignored, String field, String type, Object... values) {
        this.ignored = ignored;
        this.field = field;
        this.type = type;
        this.values = values;
    }

    public static Where equal(@NonNull String field, @NonNull Object value) {
        return new Where(true, field, "=", value);
    }

    public static Where less(@NonNull String field, @NonNull Object value) {
        return new Where(true, field, "<", value);
    }

    public static Where lessEq(@NonNull String field, @NonNull Object value) {
        return new Where(true, field, "<=", value);
    }

    public static Where great(@NonNull String field, @NonNull Object value) {
        return new Where(true, field, ">", value);
    }

    public static Where greatEq(@NonNull String field, @NonNull Object value) {
        return new Where(true, field, ">=", value);
    }

    public static Where like(@NonNull String field, @NonNull Object value) {
        return new Where(true, field, "LIKE", value);
    }

    public static Where between(@NonNull String field, @NonNull Object... values) {
        if (values.length < 2) throw new SysException("Where BETWEEN values length is less than 2");
        return new Where(true, field, "BETWEEN", values);
    }

    public static Where in(@NonNull String field, @NonNull Object... values) {
        if (values.length < 1) throw new SysException("Where IN values is empty");
        return new Where(true, field, "IN", values);
    }


    public static Where formParams(String field, String type, List<Object> values) {
        if (field == null || field.isBlank()) return null;
        if (type == null || type.isBlank() || !TYPES.contains(type.toUpperCase())) return null;
        if (values == null || values.size() == 0) return null;
        type = type.toUpperCase();
        switch (type) {
            case "=", "<", "<=", ">", ">=", "LIKE" -> {
                Object value = values.get(0);
                if (value.equals("") || value.equals("null") || value.equals("undefined")) return null;
                if (value.equals("true")) value = true;
                if (value.equals("false")) value = false;
                return new Where(true, field, type, value);
            }
            case "BETWEEN", "IN" -> {
                if (values.size() < 2) return null;
                Set<Object> vals = new HashSet<>();
                for (Object value : values) {
                    if (value.equals("") || value.equals("null") || value.equals("undefined")) continue;
                    if (value.equals("true")) vals.add(true);
                    else if (value.equals("false")) vals.add(false);
                    else vals.add(value);
                }
                if (type.equals("BETWEEN") && vals.size() < 2) return null;
                if (type.equals("IN") && vals.size() < 1) return null;
                return new Where(true, field, type, vals.toArray());
            }
            default -> {
                return null;
            }
        }
    }


    @Override
    public String toString() {
        StringBuilder valSb = new StringBuilder();
        switch (type) {
            case "=", "<", "<=", ">", ">=", "LIKE" -> {
                valSb.append(values[0].toString());
            }
            case "BETWEEN", "IN" -> {
                valSb.append("(");
                for (int i = 0; i < values.length; i++) {
                    if (i != 0) valSb.append(",");
                    valSb.append(values[i].toString());
                }
                valSb.append(")");
            }
        }
        return field + " " + type + " " + valSb;
    }

    public String getQuerySql(String table, int index) {
        StringBuilder sb = new StringBuilder(table);
        sb.append(".`").append(field).append("` ").append(type).append(" ");
        switch (type) {
            case "=", "<", "<=", ">", ">=" -> {
                sb.append("#{query.wheres[").append(index).append("].values[0]}");
            }
            case "LIKE" -> {
                sb.append("CONCAT('%',#{query.wheres[").append(index).append("].values[0]},'%')");
            }
            case "BETWEEN", "IN" -> {
                sb.append("(");
                for (int i = 0; i < values.length; i++) {
                    if (i != 0) sb.append(",");
                    sb.append("#{query.wheres[").append(index).append("].values[").append(i).append("]}");
                }
                sb.append(")");
            }
        }
        return sb.toString();
    }

    public String getSql(String table, int index) {
        return getQuerySql(table, index).replace("query.", "");
    }

}
