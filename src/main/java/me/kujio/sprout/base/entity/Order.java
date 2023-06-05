package me.kujio.sprout.base.entity;

import java.util.ArrayList;

public class Order extends ArrayList<Order.Item> {
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private Order() {}

    public Order valid(EntityHandle<?> entityHandle) {
        return this.stream()
                .filter(i -> i != null && entityHandle.containsKey(Field.snake2Camel(i.getColumn())))
                .collect(Order::new, Order::add, Order::addAll);
    }

    public static Order of(Item... items) {
        Order order = new Order();
        order.addAll(java.util.Arrays.asList(items));
        return order;
    }

    public static Order.Item item(String field, String type) {
        if (field == null || field.isBlank()) return null;
        if (type == null || type.isBlank()) return null;
        type = type.toUpperCase();
        switch (type) {
            case ASC, DESC -> {
                return new Item(field, type);
            }
            default -> {
                return null;
            }
        }
    }

    public static class Item {
        String column;
        String type;

        private Item(String field, String type) {
            this.column = Field.camel2Snake(field);
            this.type = type;
        }

        public String getColumn() {
            return column;
        }

        public String getType() {
            return type;
        }
    }
}
