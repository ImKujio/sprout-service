package me.kujio.sprout.base.entity;

import lombok.Getter;
import org.springframework.data.annotation.Transient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Field {
    private final String name;
    private final String column;
    private final Object value;
    private final boolean nullable;

    public Field(String name, Object value, boolean nullable) {
        this.name = name;
        this.column = camel2Snake(name);
        this.value = value;
        this.nullable = nullable;
    }

    @Override
    public String toString() {
        return '{' +
                "name=" + name +
                ", column=" + column +
                ", value=" + value +
                ", nullable=" + nullable +
                '}';
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
        return sb.toString();
    }

    public static String snake2Camel(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        char c;
        boolean flag = false;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (c == '_') {
                flag = true;
            } else {
                if (flag) {
                    sb.append(Character.toUpperCase(c));
                    flag = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String camel2Pascal(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String pascal2Camel(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String snake2Pascal(String str) {
        return camel2Pascal(snake2Camel(str));
    }

    public static String pascal2Snake(String str) {
        return camel2Snake(pascal2Camel(str));
    }

    public static List<String> getWritableFields(Class<?> entityClass) {
        List<String> writableFields = new ArrayList<>();
        Method[] methods = entityClass.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (isSetterMethod(methodName) && !method.isAnnotationPresent(Transient.class)) {
                String fieldName = getSetterFiled(methodName);
                if (isTransient(entityClass, fieldName)) continue;
                writableFields.add(fieldName);
            }
        }
        return writableFields;
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
        } catch (SecurityException e){
            return true;
        }
    }

}
