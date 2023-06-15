package me.kujio.sprout.base.entity;

import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Query {
    Where where;
    Order order;
    Page page;

    private final boolean ignoreErr;

    private Query(Where where, Order order, Page page,boolean ignoreErr){
        this.where = where;
        this.order = order;
        this.page = page;
        this.ignoreErr = ignoreErr;
    }

    public Query(Where where, Order order, Page page) {
        this(where,order,page,false);
    }

    public List<Where.Item> getWhere(EntityHandle<?> entityHandle) {
        return where.stream()
                .filter(i -> {
                    if (i == null || !entityHandle.containsKey(Field.snake2Camel(i.getColumn()))){
                        if (ignoreErr)
                            return false;
                        else if (i == null)
                            throw new SysException("where条件为空");
                        else
                            throw new SysException(entityHandle.entityName() + "没有字段：" + Field.snake2Camel(i.getColumn()));
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public List<Order.Item> getOrder(EntityHandle<?> entityHandle) {
        return order.stream()
                .filter(i -> {
                    if (i == null || !entityHandle.containsKey(Field.snake2Camel(i.getColumn()))){
                        if (ignoreErr)
                            return false;
                        else if (i == null)
                            throw new SysException("order条件为空");
                        else
                            throw new SysException(entityHandle.entityName() + "没有字段：" + Field.snake2Camel(i.getColumn()));
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    public Page getPage() {
        return page;
    }

    private static final String SEPARATOR = "┆";

    public static Query fromParams(Map<String, String[]> params) {
        Set<String> whereFields = new HashSet<>();
        Set<String> orderFields = new HashSet<>();

        for (String key : params.keySet()) {
            if (key == null || key.isBlank()) continue;
            if (key.startsWith("where[")) {
                int start = key.indexOf("[") + 1;
                int end = key.indexOf("]");
                whereFields.add(key.substring(start, end));
            }
            if (key.startsWith("order[")) {
                int start = key.indexOf("[") + 1;
                int end = key.indexOf("]");
                orderFields.add(key.substring(start, end));
            }
        }

        Where where = Where.of();
        for (String field : whereFields) {
            String type = params.getOrDefault("where[" + field + "][type]", new String[]{null})[0];
            String value = params.getOrDefault("where[" + field + "][value]", new String[]{null})[0];
            if (type == null || value == null) continue;
            String[] values = value.split(SEPARATOR);
            Object[] objects = new Object[values.length];
            for (int i = 0; i < values.length; i++) {
                if (Objects.equals("null", values[i])) {
                    objects[i] = null;
                } else if (Objects.equals("true", values[i])) {
                    objects[i] = true;
                } else if (Objects.equals("false", values[i])) {
                    objects[i] = false;
                } else {
                    objects[i] = values[i];
                }
            }
            try {
                where.add(Where.item(field, type, objects));
            } catch (SysException ignored) {
            }
        }

        Order order = Order.of();
        for (String field : orderFields) {
            String type = params.getOrDefault("order[" + field + "]", new String[]{"ASC"})[0];
            if (type == null) continue;
            try {
                order.add(Order.item(field, type));
            } catch (SysException ignored) {
            }
        }

        Integer size = StringUtils.toInteger(params.getOrDefault("page[size]", new String[]{null})[0]);
        Integer page = StringUtils.toInteger(params.getOrDefault("page[page]", new String[]{null})[0]);

        return new Query(where, order, size == -1 ? null : new Page(size, page),true);
    }
}
