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

    protected Set<OnUpdateHook> onUpdateHooks = new HashSet<>();

    public BaseServiceImpl(ApplicationContext context, EntityHandle<T> entityHandle) {
        this.context = context;
        this.entityHandle = entityHandle;
        this.baseMapper = context.getBean(BaseMapper.class);
        this.cacheUtils = context.getBean(CacheUtils.class);
        this.log = LoggerFactory.getLogger(getClass());
    }

    @Override
    public List<T> list(@NonNull Where where) {
        return list(new Query(where, Order.of(), new Page()));
    }

    @Override
    public List<T> list(@NonNull Where where, int limit) {
        return list(new Query(where, Order.of(), new Page(limit)));
    }

    @Override
    public List<T> list(@NonNull Where where, Order order) {
        return list(new Query(where, order, new Page()));
    }

    @Override
    public List<T> list(@NonNull Where where, Order order, int limit) {
        return list(new Query(where, order, new Page(limit)));
    }

    @Override
    public List<T> list(@NonNull Query query) {
        return baseMapper.list(
                entityHandle.getTable(),
                query.getWhere(entityHandle),
                query.getOrder(entityHandle),
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
        return count(Where.of());
    }

    @Override
    public int count(@NonNull Where where) {
        String cacheKey = entityHandle.entityName() + ": count: where=" + where;
        return cacheUtils.getOrPut(cacheKey, () -> baseMapper.count(entityHandle.getTable(), where));
    }

    @Override
    public void add(T entity) {
        AddRst addRst = new AddRst();
        baseMapper.add(entityHandle.getTable(), entityHandle.unmap(entity), addRst);
        entity.setId(addRst.getId());
        log.info("add: {}", entity);
        cacheUtils.delPrefix(entityHandle.entityName());
        for (OnUpdateHook onUpdateHook : onUpdateHooks) {
            onUpdateHook.run();
        }
    }

    @Override
    public void set(T entity) {
        if (entity.getId() == null || !exist(entity.getId()))
            throw new SysException("所修改的数据不存在");
        baseMapper.set(entityHandle.getTable(), entityHandle.unmap(entity), entity.getId());
        log.info("set: {}", entity);
        cacheUtils.delPrefix(entityHandle.entityName());
        for (OnUpdateHook onUpdateHook : onUpdateHooks) {
            onUpdateHook.run();
        }
    }

    @Override
    public void put(T entity) {
        if (entity.getId() == null) add(entity);
        else set(entity);
    }

    @Override
    public void del(Integer id) {
        del(Where.of(Where.item("id", "=", id)));
    }

    @Override
    public void del(@NonNull Where where) {
        if (where.size() == 0) throw new SysException("删除条件不能为空");
        baseMapper.del(entityHandle.getTable(), where);
        log.info("del: {}", where);
        cacheUtils.delPrefix(entityHandle.entityName());
        for (OnUpdateHook onUpdateHook : onUpdateHooks) {
            onUpdateHook.run();
        }
    }

    @Override
    public void onUpdate(OnUpdateHook onUpdateHook) {
        onUpdateHooks.add(onUpdateHook);
    }
}
