package me.kujio.sprout.base.entity;


import java.util.Set;

public class Query {
    private String[] where;
    private String[] order;
    private Integer[] pgn;

    private Set<String> removedFields;

    private Criteria criteria;
    private Sorting sorting;
    private Page page;

    public Query() {}

    public Query(Criteria criteria, Sorting sorting, Page page) {
        this.criteria = criteria;
        this.sorting = sorting;
        this.page = page;
    }

    public Criteria getCriteria(EntityHandle<? extends BaseEntity> entityHandle) {
        if (criteria != null) return criteria;
        criteria = new Criteria();
        if (where == null) return criteria;
        for (String param : where) {
            if (param == null || param.isBlank()) continue;
            Where wObj = Where.create(entityHandle, param, removedFields);
            if (wObj != null) criteria.add(wObj);
        }
        return criteria;
    }

    public Sorting getSorting(EntityHandle<? extends BaseEntity> entityHandle) {
        if (sorting != null) return sorting;
        sorting = new Sorting();
        if (order == null) return sorting;
        for (String param : order) {
            if (param == null || param.isBlank()) continue;
            Order oObj = Order.create(entityHandle, param, removedFields);
            if (oObj != null) sorting.add(oObj);
        }
        return sorting;
    }

    public Page getPage() {
        if (page != null) return page;
        this.page = Page.create(pgn);
        return page;
    }

    public void setWhere(String[] where) {
        this.where = where;
    }

    public void setOrder(String[] order) {
        this.order = order;
    }

    public void setPgn(Integer[] pgn) {
        this.pgn = pgn;
    }

    public void remove(String... fields) {
        removedFields = Set.of(fields);
    }

}