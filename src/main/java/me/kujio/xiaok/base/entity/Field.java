package me.kujio.xiaok.base.entity;

import lombok.Getter;

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

}
