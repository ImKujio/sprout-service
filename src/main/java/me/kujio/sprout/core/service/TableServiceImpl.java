package me.kujio.sprout.core.service;

import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.entity.Where;
import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.core.mapper.TableMapper;
import me.kujio.sprout.core.table.TableRecord;
import me.kujio.sprout.core.table.TableSchema;
import me.kujio.sprout.utils.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TableServiceImpl<T> implements TableService<T> {
    private String _entityName;
    private Logger _logger;
    private TableSchema _tableSchema;
    private TableMapper _tableMapper;


    protected String entityName() {
        if (_entityName != null) return _entityName;
        String curName = getClass().getSimpleName();
        _entityName = curName.substring(0, curName.indexOf("ServiceImpl"));
        return _entityName;
    }

    protected Logger logger() {
        if (_logger != null) return _logger;
        _logger = LoggerFactory.getLogger(getClass());
        return _logger;
    }

    protected TableSchema tableSchema() {
        if (_tableSchema != null) return _tableSchema;
        _tableSchema = TableRecord.getTableSchema(entityName());
        return _tableSchema;
    }

    protected TableMapper tableMapper() {
        if (_tableMapper != null) return _tableMapper;
        _tableMapper = TableMapper().Holder.get();
        return _tableMapper;
    }

    @Override
    public List<T> list(Query query) {
        validQuery(query);
        return CacheUtils.getOrPut(queryKey("list", query),
                () -> tableMapper().list(entityName(), query));
    }

    @Override
    public List<T> list(List<Where> wheres) {
        return list(new Query().add(wheres));
    }

    @Override
    public Map<Integer, T> map(Query query) {
        validQuery(query);
        return CacheUtils.getOrPut(queryKey("map", query),
                () -> tableMapper().map(entityName(), query));
    }

    @Override
    public Map<Integer, T> map(List<Where> wheres) {
        return map(new Query().add(wheres));
    }

    @Override
    public int count(Query query) {
        return count(query.getWheres());
    }

    @Override
    public int count(List<Where> wheres) {
        validWheres(wheres);
        return CacheUtils.getOrPut(wheresKey("count", wheres),
                () -> tableMapper().count(entityName(), wheres));
    }

    @Override
    public Map<Integer, T> all(List<String> fields) {
        validFields(fields);
        return CacheUtils.getOrPut(fieldsKey("all", fields),
                () -> tableMapper().all(entityName(), fields));
    }

    @Override
    public T get(Integer pkValue) {
        return get(tableSchema().getPrimaryKey(), pkValue);
    }

    @Override
    public T get(String field, Object value) {
        validField(field);
        T t = tableMapper().get(entityName(), field, value);
        return t;
    }

    @Override
    public void add(T entity) {
        add(List.of(entity));
    }

    @Override
    public void add(List<T> entities) {
        tableMapper().add(entities);
        logger().info("add:{}", entities);
        clearCache();
    }

    @Override
    public void set(T entity) {
        set(entity, new String[]{});
    }

    @Override
    public void set(T entity, String... fixFields) {
        tableMapper().set(entity, fixFields);
        logger().info("set:{},fix:{}", entity, fixFields);
        clearCache();
    }

    @Override
    public void put(T entity) {
        Integer pkValue = tableSchema().getPrimaryValue(entity);
        if (pkValue == null) {
            if (tableSchema().isIncrement()) add(entity);
            else throw new SysException("The primary key value of table that does not increment cannot be empty");
        } else {
            if (tableSchema().isIncrement() || exist(pkValue)) set(entity);
            else add(entity);
        }
    }

    @Override
    public void put(List<T> entities, String field, Object value) {
        put(entities, List.of(Where.equal(field, value)));
    }

    @Override
    public void put(List<T> entities, List<Where> wheres) {
        Map<Integer, T> rawList = map(wheres);
        List<T> addList = new ArrayList<>();
        Set<Integer> retain = new HashSet<>();
        for (T entity : entities) {
            Integer pkValue = tableSchema().getPrimaryValue(entity);
            if (pkValue == null) {
                if (tableSchema().isIncrement()) addList.add(entity);
                else throw new SysException("The primary key value of table that does not increment cannot be empty");
            } else if (rawList.containsKey(pkValue)) {
                retain.add(pkValue);
                if (rawList.get(pkValue).equals(entity)) continue;
                set(entity);
            } else {
                if (_tableSchema.isIncrement())
                    throw new SysException("The data contains a non-existent primary key");
                else addList.add(entity);
            }
        }
        Set<Integer> delSet = rawList.keySet();
        delSet.removeAll(retain);
        if (delSet.size() > 0) {
            del(List.of(Where.in(_tableSchema.getPrimaryKey(), delSet.toArray())));
        }
        if (addList.size() > 0) {
            add(addList);
        }
    }

    @Override
    public void del(Integer pkValue) {
        del(List.of(Where.equal(tableSchema().getPrimaryKey(), pkValue)));
    }

    @Override
    public void del(List<Where> wheres) {
        tableMapper().del(entityName(), wheres);
        logger().info("del:{}", wheres);
        clearCache();
    }

    @Override
    public boolean exist(Integer pkValue) {
        return exist(tableSchema().getPrimaryKey(), pkValue);
    }

    @Override
    public boolean exist(String field, Object value) {
        return tableMapper().exist(entityName(), field, value);
    }

    @Override
    public String cacheKey(String... tags) {
        return entityName() + ":" + String.join(":", tags);
    }

    public void clearCache() {
        CacheUtils.delPrefix(entityName());
    }

    protected String fieldsKey(String method, List<String> fields) {
        Collections.sort(fields);
        return entityName() + ":" + method + ":" + String.join(",", fields);
    }

    protected String queryKey(String method, Query query) {
        return entityName() + ":" + method + ":" + query;
    }

    protected String wheresKey(String method, List<Where> wheres) {
        return queryKey(method, new Query().add(wheres));
    }

    protected void validField(String field) {
        if (!tableSchema().hasField(field)) throw new SysException("Field name is error:" + field);
    }

    protected void validFields(List<String> fields) {
        fields.removeIf(field -> !tableSchema().hasField(field));
    }

    protected void validQuery(Query query) {
        validWheres(query.getWheres());
        query.getOrders().removeIf(order -> {
            if (tableSchema().hasField(order.getField())) return false;
            else if (order.isIgnored()) return true;
            else throw new SysException("Order field name is error:" + order);
        });
    }

    protected void validWheres(List<Where> wheres) {
        wheres.removeIf(where -> {
            if (tableSchema().hasField(where.getField())) return false;
            else if (where.isIgnored()) return true;
            else throw new SysException("Where field name is error:" + where);
        });
    }

}
