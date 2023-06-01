package me.kujio.xiaok.base.entity;

import lombok.Getter;
import org.slf4j.Logger;

import java.util.Set;


@Getter
public class Order {
    private String column;
    private String type;

    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private Order(String column, String type) {
        this.column = column;
        this.type = type;
    }

    private Order() {
    }

    public static Order create(Logger log, EntityHandle<?> entityHandle, String field, String type) {
        if (field == null || field.isBlank() || !entityHandle.containsKey(field) || (!type.equals(ASC) && !type.equals(DESC))) {
            log.error("order条件错误：{} {}", field, type);
            return null;
        }
        return new Order(Field.camel2Snake(field), type);
    }

    public static Order create(EntityHandle<?> entityHandle, String params, Set<String> removedFields) {
        if (params == null || params.isBlank()) return null;
        String[] split = params.split("\\|");
        if (split.length < 1 || !entityHandle.containsKey(split[0])) return null;
        if (removedFields != null && removedFields.contains(split[0])) return null;
        String column = Field.camel2Snake(split[0]);
        if (split.length == 1) return new Order(column, ASC);
        String type = split[1].toUpperCase();
        if (type.equalsIgnoreCase(ASC) || type.equalsIgnoreCase(DESC)) {
            return new Order(column, type);
        }
        return null;
    }

    @Override
    public String toString() {
        return '{' +
                "column=" + column +
                ", type=" + type +
                '}';
    }
}
