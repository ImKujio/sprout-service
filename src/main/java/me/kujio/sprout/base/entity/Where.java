package me.kujio.sprout.base.entity;

import me.kujio.sprout.core.exception.SysException;

import java.util.ArrayList;
import java.util.Arrays;

public class Where extends ArrayList<Where.Item> {

    private Where() {}

    public static Where of(Item... items) {
        Where where = new Where();
        where.addAll(Arrays.asList(items));
        return where;
    }

    public static Where.Item item(String field, String type, Object... value) {
        if (field == null || field.isBlank()) throw new SysException("Where构造失败：field为空");
        if (type == null || type.isBlank()) throw new SysException("Where构造失败：type为空");
        if (value == null || value.length == 0) throw new SysException("Where构造失败：value为空");
        type = type.toUpperCase();
        switch (type) {
            case "=", ">", ">=", "<", "<=", "LIKE" -> {
                return new Item(field, type, Arrays.copyOfRange(value, 0, 1));
            }
            case "IN" -> {
                return new Item(field, type, value);
            }
            case "BETWEEN" -> {
                if (value.length < 2) throw new SysException("Where构造失败：between条件时，value数量小于2");
                return new Item(field, type, Arrays.copyOfRange(value, 0, 2));
            }
            default -> {
                return null;
            }
        }
    }

    public static class Item {
        private final String column;
        private final String type;
        private final Object[] value;

        private Item(String field, String type, Object[] value) {
            this.column = Field.camel2Snake(field);
            this.type = type;
            this.value = value;
        }

        public String getColumn() {
            return column;
        }

        public String getType() {
            return type;
        }

        public Object getValue() {
            return value[0];
        }

        public Object[] getValues() {
            return value;
        }
    }
}
