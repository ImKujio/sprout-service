package me.kujio.sprout.core.table;

import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.utils.EntityUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    public static TableSchema getTableSchema(String entityName) {
        TableSchema schema = tablesMap.get(entityName);
        if (schema == null) throw new SysException("找不到类：" + entityName + "的表结构");
        return schema;
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
                if (className != null) {
                    Class<?> entityClass = Class.forName(className);
                    Table table = entityClass.getAnnotation(Table.class);
                    if (table == null) continue;
                    List<TableColumn> columns = EntityUtil.getProps(entityClass).stream()
                            .map(s -> new TableColumn(EntityUtil.camel2Snake(s), s)).toList();
                    tablesMap.put(entityClass.getSimpleName(), new TableSchema(table.value(), columns));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String determineClassName(Resource resource, String prefix) throws IOException {
        String classFile = resource.getURI().toString();
        if (classFile == null) return null;
        if (classFile.contains(prefix) && classFile.contains(".class")) {
            return classFile.substring(classFile.indexOf(prefix), classFile.indexOf(".class")).replace('/', '.');
        }
        return null;
    }

}
