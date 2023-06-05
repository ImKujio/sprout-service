package me.kujio.sprout.base.entity;

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
        if (field == null || field.isBlank()) return null;
        if (type == null || type.isBlank()) return null;
        if (value == null || value.length == 0) return null;
        type = type.toUpperCase();
        switch (type) {
            case "=", ">", ">=", "<", "<=", "LIKE" -> {
                return new Item(field, type, Arrays.copyOfRange(value, 0, 1));
            }
            case "BETWEEN" -> {
                if (value.length < 2) return null;
                return new Item(field, type, Arrays.copyOfRange(value, 0, 2));
            }
            case "IN" -> {
                if (value.length < 2) return null;
                return new Item(field, type, value);
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
