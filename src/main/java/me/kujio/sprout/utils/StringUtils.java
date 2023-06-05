package me.kujio.sprout.utils;

public class StringUtils {
    public static Integer toInteger(String str) {
        if (str == null || str.isBlank()) return null;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
