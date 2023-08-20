package me.kujio.sprout.core.table;

import java.lang.reflect.Method;

public record TableColumn(
        String column,
        String field,
        Method getterMethod,
        Method setterMethod,
        Class<?> type
) {
}