package me.kujio.sprout.core.service;

import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.entity.Where;
import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.core.mapper.TableMapper;
import me.kujio.sprout.core.table.TableMap;
import me.kujio.sprout.core.table.TableRecord;
import me.kujio.sprout.core.table.TableSchema;
import me.kujio.sprout.utils.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

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
        _tableMapper = TableMapper.Holder.get();
        return _tableMapper;
    }

    @Override
    public List<T> list(List<Where> wheres) {
        return list(new Query().add(wheres));
    }

    @Override
    public List<T> list(Query query) {
        validQuery(query);
        return CacheUtils.getOrPut(cacheKey("list", query.toString()),
                () -> parseList(tableMapper().list(entityName(), query)));
    }

    @Override
    public Map<Integer, T> dict(List<String> fields) {
        return dict(Query.dict(fields));
    }

    @Override
    public Map<Integer, T> dict(Query query) {
        validQuery(query);
        return CacheUtils.getOrPut(cacheKey("dict", query.toString()),
                () -> parseDict(tableMapper().list(entityName(), query)));
    }



    @Override
    public int count(List<Where> wheres) {
        return count(new Query().add(wheres));
    }

    @Override
    public int count(Query query) {
        validQuery(query);
        return CacheUtils.getOrPut(cacheKey("count", query.toString()),
                () -> tableMapper().count(entityName(), query.getWheres()));
    }

    @Override
    public T get(Integer pkValue) {
        return get(tableSchema().getPrimaryKey(),pkValue);
    }

    @Override
    public T get(String field, Object value) {
        return parseEntity(tableMapper().get(entityName(), field, value));
    }

    @Override
    public void add(T entity) {
        add(List.of(entity));
    }

    @Override
    public void add(List<T> entities) {
        tableMapper().add(entityName(), entities);
        logger().info("add:{}", entities);
        clearCache();
    }

    @Override
    public void set(T entity) {
        set(entity,List.of());
    }

    @Override
    public void set(T entity, List<String> fixFields) {
        tableMapper().set(entityName(), entity, fixFields);
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
        Map<Integer, T> rawList = dict(new Query().add(wheres));
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
        return tableMapper().count(entityName(),List.of(Where.equal(field,value))) > 0;
    }

    @Override
    public String cacheKey(String... tags) {
        return entityName() + ":" + String.join(":", tags);
    }

    public void clearCache() {
        CacheUtils.delPrefix(entityName());
    }

    protected void validQuery(Query query) {
        query.getWheres().removeIf(where -> {
            if (tableSchema().hasField(where.getField())) return false;
            else if (where.isIgnored()) return true;
            else throw new SysException("Where field name is error:" + where);
        });
        query.getOrders().removeIf(order -> {
            if (tableSchema().hasField(order.getField())) return false;
            else if (order.isIgnored()) return true;
            else throw new SysException("Order field name is error:" + order);
        });
        query.getFields().removeIf(field -> {
            if (tableSchema().hasField(field)) return false;
            else if (query.isIgnored()) return true;
            else throw new SysException("Query field name is error:" + field);
        });
    }

    @Override
    public T parseEntity(TableMap tableMap) {
        return tableMap.map(tableSchema());
    }

    @Override
    public List<T> parseList(List<TableMap> tableMaps) {
        return tableMaps.stream().map(this::parseEntity).toList();
    }

    @Override
    public Map<Integer, T> parseDict(List<TableMap> tableMaps) {
        return tableMaps.stream().map(this::parseEntity).collect(Collectors.toMap(
                entity -> tableSchema().getPrimaryValue(entity),
                entity -> entity
        ));
    }
}
