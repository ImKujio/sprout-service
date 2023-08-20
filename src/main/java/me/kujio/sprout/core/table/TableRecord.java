package me.kujio.sprout.core.table;

import me.kujio.sprout.core.exception.SysException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TableRecord {
    private static final Map<String, TableSchema> tablesMap = new HashMap<>();
    private final ResourceLoader resourceLoader;

    public TableRecord(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void record() {
        String basePackage = "me.kujio.sprout"; // 替换为你的实际包名
        String packagePath = basePackage.replace('.', '/');
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packagePath + "/**/entity/*.class";
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                String className = determineClassName(resource, packagePath);
                if (className == null) continue;
                Class<?> entityClass = Class.forName(className);
                Table table = entityClass.getAnnotation(Table.class);
                if (table == null) continue;
                Constructor<?> constructor = entityClass.getConstructor();
                List<TableColumn> columns = getTableColumns(entityClass);
                String tableName = table.value().isBlank() ? pascal2Snake(entityClass.getSimpleName()) : table.value();
                TableSchema tableSchema = new TableSchema(tableName, table.primaryKey(), table.increment(), columns, constructor);
                tablesMap.put(entityClass.getSimpleName(), tableSchema);
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static TableSchema getTableSchema(String entityName) {
        TableSchema schema = tablesMap.get(entityName);
        if (schema == null) throw new SysException("找不到类：" + entityName + "的表结构");
        return schema;
    }

    private static String determineClassName(Resource resource, String prefix) throws IOException {
        String classFile = resource.getURI().toString();
        if (classFile == null) return null;
        if (classFile.contains(prefix) && classFile.contains(".class")) {
            return classFile.substring(classFile.indexOf(prefix), classFile.indexOf(".class")).replace('/', '.');
        }
        return null;
    }

    private static List<TableColumn> getTableColumns(Class<?> entityClass) {
        List<TableColumn> columns = new ArrayList<>();
        for (Method getterMethod : entityClass.getMethods()) {
            String methodName = getterMethod.getName();
            if (isGetterMethod(getterMethod)) {
                String fieldName = getGetterFiled(methodName);
                if (isTransientField(entityClass, fieldName)) continue;
                Method settterMethod = getSetterMethod(entityClass, getterMethod);
                if (settterMethod == null) continue;
                String column = camel2Snake(fieldName);
                columns.add(
                        new TableColumn(
                                column,
                                fieldName,
                                getterMethod,
                                settterMethod,
                                settterMethod.getParameterTypes()[0])
                );
            }
        }
        return columns;
    }

    private static boolean isGetterMethod(Method method) {
        String methodName = method.getName();
        Class<?> returnType = method.getReturnType();
        return methodName.startsWith("get") && methodName.length() > 3 && method.getParameterCount() == 0
                && !method.isAnnotationPresent(Transient.class)
                && returnType != void.class && returnType != Class.class;
    }

    private static Method getSetterMethod(Class<?> entityClass, Method getterMethod) {
        try {
            String methodName = getterMethod.getName().replace("get", "set");
            Class<?> paramType = getterMethod.getReturnType();
            return entityClass.getMethod(methodName, paramType);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isTransientField(Class<?> entityClass, String fieldName) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            return field.isAnnotationPresent(Transient.class);
        } catch (NoSuchFieldException e) {
            return false;
        } catch (SecurityException e) {
            return true;
        }
    }

    private static String getGetterFiled(String methodName) {
        String fieldName = methodName.substring(3);
        return Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private static String camel2Snake(String str) {
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

}
