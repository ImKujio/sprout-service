package me.kujio.sprout.core.table;


import java.util.List;

public class TableSchema {
    private final String name;
    private final List<TableColumn> columns;
    private final String selectSql;

    public TableSchema(String name, List<TableColumn> columns) {
        this.name = name;
        this.columns = columns;
        StringBuilder rawSb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            TableColumn col = columns.get(i);
            if (i != 0) {
                rawSb.append(",");
            }
            rawSb.append(name).append(".`").append(col.column()).append("` AS `").append(col.prop()).append("`");
        }
        this.selectSql = rawSb.toString();
    }

    public String getName() {
        return name;
    }

    public List<TableColumn> getColumns() {
        return columns;
    }

    public String getSelectSql() {
        return selectSql;
    }

    @Override
    public String toString() {
        return "TableSchema{" +
                "name='" + name + '\'' +
                ", columns=" + columns +
                ", selectSql='" + selectSql + '\'' +
                '}';
    }
}
