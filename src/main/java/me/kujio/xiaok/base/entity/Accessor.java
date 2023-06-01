package me.kujio.xiaok.base.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 实体类字段访问器
 * @param <T>
 */
public final class Accessor<T extends BaseEntity> {
    private final Getter<T> getter;
    private final Setter<T> setter;

    public Accessor(Getter<T> getter, Setter<T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public Object get(T t) {
        return getter.get(t);
    }

    public void set(T t, Object value) {
        setter.set(t, value);
    }

    public interface Getter<T extends BaseEntity> {
        Object get(T t);
    }

    public interface Setter<T extends BaseEntity> {
        void set(T t, Object value);
    }

    public interface IntSetter<T extends BaseEntity> {
        void set(T t, Integer value);
    }

    public interface BoolSetter<T extends BaseEntity> {
        void set(T t, Boolean value);
    }

    public interface StrSetter<T extends BaseEntity> {
        void set(T t, String value);
    }

    public interface DecSetter<T extends BaseEntity> {
        void set(T t, BigDecimal value);
    }

    public interface LdtSetter<T extends BaseEntity> {
        void set(T t, LocalDateTime value);
    }

    public interface LdSetter<T extends BaseEntity> {
        void set(T t, LocalDate value);
    }

    public interface LtSetter<T extends BaseEntity> {
        void set(T t, LocalTime value);
    }

}


