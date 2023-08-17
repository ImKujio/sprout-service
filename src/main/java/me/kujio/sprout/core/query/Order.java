package me.kujio.sprout.core.query;

import lombok.Getter;
import lombok.NonNull;
import me.kujio.sprout.core.exception.SysException;

@Getter
public final class Order {
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    private final String field;
    private final String type;
    private final boolean ignored;


    private Order(boolean ignored,String field, String type) {
        this.ignored = ignored;
        this.field = field;
        this.type = type;
    }

    public static Order asc(@NonNull String field){
        if (field.isBlank()) throw new SysException("Order field is blank");
        return new Order(false,field, ASC);
    }

    public static Order desc(@NonNull String field){
        if (field.isBlank()) throw new SysException("Order field is blank");
        return new Order(false,field, DESC);
    }

    public static Order formParams(String field, String type){
        if (field == null || field.isBlank()) return null;
        type = type == null ? ASC : type.equalsIgnoreCase(DESC) ? DESC : ASC;
        return new Order(true,field,type);
    }


    @Override
    public String toString() {
        return field + " " + type;
    }

}
