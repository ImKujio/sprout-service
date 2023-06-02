package me.kujio.sprout.base.service.impl;

import lombok.NonNull;
import me.kujio.sprout.base.entity.*;
import me.kujio.sprout.base.mapper.BaseMapper;
import me.kujio.sprout.base.service.BaseService;
import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.utils.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    protected final ApplicationContext context;
    protected final EntityHandle<T> entityHandle;
    protected final BaseMapper baseMapper;
    protected final CacheUtils cacheUtils;
    protected final Logger log;

    public BaseServiceImpl(ApplicationContext context,EntityHandle<T> entityHandle) {
        this.context = context;
        this.entityHandle = entityHandle;
        this.baseMapper = context.getBean(BaseMapper.class);
        this.cacheUtils = context.getBean(CacheUtils.class);
        this.log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public List<T> list(@NonNull Criteria criteria) {
        return list(new Query(criteria, sorting(), new Page()));
    }

    @Override
    public List<T> list(@NonNull Criteria criteria, int limit) {
        return list(new Query(criteria, sorting(), new Page(limit)));
    }

    @Override
    public List<T> list(@NonNull Criteria criteria, Sorting sorting) {
        return list(new Query(criteria, sorting, new Page()));
    }

    @Override
    public List<T> list(@NonNull Criteria criteria, Sorting sorting, int limit) {
        return list(new Query(criteria, sorting, new Page(limit)));
    }

    @Override
    public List<T> list(@NonNull Query query) {
        return baseMapper.list(
                entityHandle.getTable(),
                query.getCriteria(entityHandle),
                query.getSorting(entityHandle),
                query.getPage()
        ).stream().map(entityHandle::map).collect(Collectors.toList());
    }

    @Override
    public T get(Integer id) {
        Map<String, Object> row = baseMapper.get(entityHandle.getTable(), id);
        return entityHandle.map(row);
    }

    @Override
    public Map<Integer, Map<String, Object>> all(Set<String> fieldNames) {
        List<Field> fields = entityHandle.toFields(fieldNames);
        String cacheKey = entityHandle.entityName() + ": all: fields=" + fields;
        return cacheUtils.getOrPut(cacheKey, () -> baseMapper.all(entityHandle.getTable(), fields));
    }

    @Override
    public boolean exist(Integer id) {
        return baseMapper.exist(entityHandle.getTable(), id) > 0;
    }

    @Override
    public int total() {
        return count(criteria());
    }

    @Override
    public int count(@NonNull Criteria criteria) {
        String cacheKey = entityHandle.entityName() + ": count: criteria=" + criteria;
        return cacheUtils.getOrPut(cacheKey, () -> baseMapper.count(entityHandle.getTable(), criteria));
    }

    @Override
    public void add(T entity) {
        AddRst addRst = new AddRst();
        baseMapper.add(entityHandle.getTable(), entityHandle.unmap(entity), addRst);
        entity.setId(addRst.getId());
        cacheUtils.delPrefix(entityHandle.entityName());
        log.info("add: {}", entity);
    }

    @Override
    public void set(T entity) {
        if (entity.getId() == null || !exist(entity.getId()))
            throw new SysException("所修改的数据不存在");
        baseMapper.set(entityHandle.getTable(), entityHandle.unmap(entity), entity.getId());
        cacheUtils.delPrefix(entityHandle.entityName());
        log.info("set: {}", entity);
    }

    @Override
    public void put(T entity) {
        if (entity.getId() == null) add(entity);
        else set(entity);
    }

    @Override
    public void del(Integer id) {
        del(criteria(where("id", "=", id)));
    }

    @Override
    public void del(@NonNull Criteria criteria) {
        if (criteria.size() == 0)
            throw new SysException("删除条件不能为空");
        baseMapper.del(entityHandle.getTable(), criteria);
        cacheUtils.delPrefix(entityHandle.entityName());
        log.info("del: {}", criteria);
    }


    /**
     * 快速创建查询条件 <br>
     * 示例：criteria(where("id", "=", 1), where("age", "between", 15,18))
     * @param wheres 单个条件
     * @return 查询条件
     */
    protected Criteria criteria(Where... wheres) {
        return new Criteria() {{
            for (Where where : wheres) {
                if (where != null) add(where);
            }
        }};
    }

    protected Where where(String field, String type, Object... values) {
        return Where.create(log, entityHandle, field, type, values);
    }

    /**
     * 快速创建排序条件 <br>
     * 示例：sorting(asc("id"), desc("age"))
     * @param orders 单个排序
     * @return 排序条件
     */
    protected Sorting sorting(Order... orders) {
        return new Sorting() {{
            for (Order order : orders) {
                if (order != null) add(order);
            }
        }};
    }

    protected Order asc(String field) {
        return Order.create(log, entityHandle, field, Order.ASC);
    }

    protected Order desc(String field) {
        return Order.create(log, entityHandle, field, Order.DESC);
    }

}
