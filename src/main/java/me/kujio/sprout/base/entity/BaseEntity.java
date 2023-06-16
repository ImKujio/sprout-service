package me.kujio.sprout.base.entity;


import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Set<String> nullable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNullable(Set<String> nullable) {
        this.nullable = nullable;
    }

    public boolean isNullable(String field) {
        if (nullable == null) return false;
        return nullable.contains(field);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        return Objects.equals(id, that.id);
    }
}
