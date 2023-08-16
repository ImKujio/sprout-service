package me.kujio.sprout.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
class User extends Table{
    private String name;
    private Integer age;
    @Transient
    private LocalDate brith;

    public static void main(String[] args) {
        User user = new User();
        System.out.println(user.getTable());
        System.out.println(user.getProps());
    }
}

record Prop(String name, String column) {
}


public class Table {
    private final List<Prop> props;
    private final String table;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Prop> getProps() {
        return props;
    }

    public String getTable() {
        return table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(id, table.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Table() {
        Class<? extends Table> clazz = this.getClass();
        table = pascal2Camel(clazz.getSimpleName());
        props = getProps(clazz);
    }

    public static List<Prop> getProps(Class<?> entityClass) {
        List<Prop> props = new ArrayList<>();
        Method[] methods = entityClass.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            System.out.println(methodName);
            if (isSetterMethod(methodName) && !method.isAnnotationPresent(Transient.class)) {
                String fieldName = getSetterFiled(methodName);
                if (isTransient(entityClass, fieldName)) continue;
                props.add(new Prop(fieldName, camel2Snake(fieldName)));
            }
        }
        return props;
    }

    public static String camel2Snake(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString().toLowerCase();
    }

    public static String pascal2Camel(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String pascal2Snake(String str) {
        return camel2Snake(pascal2Camel(str));
    }

    private static boolean isSetterMethod(String methodName) {
        return methodName.startsWith("set") && methodName.length() > 3;
    }

    private static String getSetterFiled(String methodName) {
        String fieldName = methodName.substring(3);
        return Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private static boolean isTransient(Class<?> entityClass, String fieldName) {
        try {
            java.lang.reflect.Field field = entityClass.getDeclaredField(fieldName);
            return field.isAnnotationPresent(Transient.class);
        } catch (NoSuchFieldException e) {
            return false;
        } catch (SecurityException e) {
            return true;
        }
    }
}
