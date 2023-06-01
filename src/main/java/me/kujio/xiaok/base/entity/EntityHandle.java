package me.kujio.xiaok.base.entity;

import me.kujio.xiaok.utils.TimeUtils;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class EntityHandle<T extends BaseEntity> extends HashMap<String, Accessor<T>> {

    private EntityGetter<T> getter;
    private String table;
    private String name;

    public T newEntity(){
        return getter.get();
    }

    public String getTable() {
        return table;
    }

    public String entityName(){
        return name;
    }

    protected void getter(EntityGetter<T> getter){
        T entity = getter.get();
        this.getter = getter;
        this.name = entity.getClass().getSimpleName();
        this.table = Field.pascal2Snake(name);
    }

    protected void getter(EntityGetter<T> getter, String table){
        T entity = getter.get();
        this.getter = getter;
        this.name = entity.getClass().getSimpleName();
        this.table = table;
    }

    /**
     * 从数据库中取出的数据，映射到实体类中
     *
     * @param rows   数据库中取出的数据
     * @return 实体类
     */
    public T map(Map<String, Object> rows) {
        T entity = getter.get();
        rows.forEach((k, v) -> {
            String field = Field.snake2Camel(k);
            if (this.containsKey(field)) {
                this.get(field).set(entity, v);
            }
        });
        return entity;
    }

    /**
     * 将实体类中的数据映射到数据库列信息
     *
     * @param entity 实体类
     * @return 数据库列信息
     */
    public List<Field> unmap(T entity) {
        return this.entrySet().stream()
                .map(entry -> new Field(entry.getKey(), entry.getValue().get(entity), entity.isNullable(entry.getKey())))
                .collect(Collectors.toList());
    }

    public List<Field> toFields(Set<String> fields){
        return this.keySet().stream()
                .filter(s -> fields.contains(s) && !s.equals("id"))
                .map(accessor -> new Field(accessor, null, false))
                .sorted(Comparator.comparing(Field::toString))
                .collect(Collectors.toList());
    }

    public interface EntityGetter<T extends BaseEntity>{
        T get();
    }

    protected static <T extends BaseEntity> Accessor<T> accessor(Accessor.Getter<T> getter, Accessor.IntSetter<T> setter) {
        return new Accessor<>(getter, (t, val) -> {
            if (val instanceof Integer) setter.set(t, (Integer) val);
        });
    }

    protected static <T extends BaseEntity> Accessor<T> accessor(Accessor.Getter<T> getter, Accessor.BoolSetter<T> setter) {
        return new Accessor<>(getter, (t, val) -> {
            if (val instanceof Boolean) setter.set(t, Objects.equals(val, true));
            if (val instanceof Integer) setter.set(t, Objects.equals(val, 1));
        });
    }

    protected static <T extends BaseEntity> Accessor<T> accessor(Accessor.Getter<T> getter, Accessor.StrSetter<T> setter) {
        return new Accessor<>(getter, (t, val) -> {
            if (val instanceof String) setter.set(t, (String) val);
        });
    }

    protected static <T extends BaseEntity> Accessor<T> accessor(Accessor.Getter<T> getter, Accessor.DecSetter<T> setter) {
        return new Accessor<>(getter, (t, val) -> {
            if (val instanceof BigDecimal) setter.set(t, (BigDecimal) val);
        });
    }

    protected static <T extends BaseEntity> Accessor<T> accessor(Accessor.Getter<T> getter, Accessor.LdtSetter<T> setter) {
        return new Accessor<>(getter, (t, val) -> {
            if (val instanceof LocalDateTime) setter.set(t, (LocalDateTime) val);
        });
    }

    protected static <T extends BaseEntity> Accessor<T> accessor(Accessor.Getter<T> getter, Accessor.LdSetter<T> setter) {
        return new Accessor<>(getter, (t, val) -> {
            if (val instanceof LocalDate) setter.set(t, (LocalDate) val);
            if (val instanceof Date) setter.set(t, TimeUtils.date2Ld((Date) val));
        });
    }

    protected static <T extends BaseEntity> Accessor<T> accessor(Accessor.Getter<T> getter, Accessor.LtSetter<T> setter) {
        return new Accessor<>(getter, (t, val) -> {
            if (val instanceof LocalTime) setter.set(t, (LocalTime) val);
            if (val instanceof Time) setter.set(t, TimeUtils.time2Lt((Time) val));
        });
    }
}
