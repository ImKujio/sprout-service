package me.kujio.sprout.core.entity;

import lombok.Getter;
import lombok.NonNull;
import me.kujio.sprout.utils.StringUtils;

import java.util.*;

@Getter
public class Query {
    private final List<Where> wheres = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private Page page;


    public Query add(@NonNull Where where) {
        wheres.add(where);
        return this;
    }

    public Query add(@NonNull Where... wheres) {
        this.wheres.addAll(Arrays.asList(wheres));
        return this;
    }

    public Query add(@NonNull List<Where> wheres) {
        this.wheres.addAll(wheres);
        return this;
    }

    public Query add(@NonNull Order order) {
        orders.add(order);
        return this;
    }

    public Query add(@NonNull Page page) {
        this.page = page;
        return this;
    }

    public static Query all(){
        return new Query();
    }

    public static Query fromParams(Map<String, String[]> params) {
        Query query = new Query();
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
        for (String field : whereFields) {
            String type = params.getOrDefault("where[" + field + "][type]", new String[]{null})[0];
            String value = params.getOrDefault("where[" + field + "][value]", new String[]{null})[0];
            if (type == null || type.isBlank() || value == null || value.isBlank()) continue;
            String[] values = value.split(Where.SEPARATOR);
            Where where = Where.formParmas(field, type, (Object) values);
            if (where != null) query.add(where);
        }

        for (String field : orderFields) {
            String type = params.getOrDefault("order[" + field + "]", new String[]{"ASC"})[0];
            Order order = Order.formParams(field, type);
            if (order != null) query.add(order);
        }

        Integer size = StringUtils.toInteger(params.getOrDefault("page[size]", new String[]{null})[0]);
        Integer page = StringUtils.toInteger(params.getOrDefault("page[page]", new String[]{null})[0]);

        if (size != null && size == -1) return query;
        return query.add(new Page(size, page));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> whereLines = wheres.stream().map(Where::toString).sorted().toList();
        for (String where : whereLines) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(where);
        }
        List<String> orderLines = orders.stream().map(Order::toString).sorted().toList();
        for (String order : orderLines) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(order);
        }
        if (page != null) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(page.toString());
        }
        return sb.toString();
    }
}
