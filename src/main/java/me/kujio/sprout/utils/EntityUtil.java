package me.kujio.sprout.utils;

import org.springframework.data.annotation.Transient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class EntityUtil {

    public static List<String> getProps(Class<?> entityClass) {
        List<String> props = new ArrayList<>();
        Method[] methods = entityClass.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (isSetterMethod(methodName) && !method.isAnnotationPresent(Transient.class)) {
                String fieldName = getSetterFiled(methodName);
                if (isTransient(entityClass, fieldName)) continue;
                props.add(fieldName);
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
