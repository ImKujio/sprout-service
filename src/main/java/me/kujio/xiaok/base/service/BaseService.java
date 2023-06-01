package me.kujio.xiaok.base.service;

import lombok.NonNull;
import me.kujio.xiaok.base.entity.*;

import java.util.*;

public interface BaseService<T extends BaseEntity> {

    /**
     * 根据条件查询数据集合
     * @param criteria 条件
     * @return 数据集合
     */
    List<T> list(@NonNull Criteria criteria);

    /**
     * 根据条件查询数据集合
     * @param criteria 条件
     * @param limit 限制条数
     * @return 数据集合
     */
    List<T> list(@NonNull Criteria criteria,int limit);

    /**
     * 根据条件查询数据集合
     * @param criteria 条件
     * @param sorting 排序
     * @return 数据集合
     */
    List<T> list(@NonNull Criteria criteria, Sorting sorting);

    /**
     * 根据条件查询数据集合
     * @param criteria 条件
     * @param sorting 排序
     * @param limit 限制条数
     * @return 数据集合
     */
    List<T> list(@NonNull Criteria criteria, Sorting sorting, int limit);

    /**
     * 根据条件查询数据集合
     * @param query 查询条件集
     * @return 数据集合
     */
    List<T> list(@NonNull Query query);

    /**
     * 根据ID查询数据
     * @param id 编号
     * @return 数据
     */
    T get(Integer id);

    /**
     * 根据字段查询所有数据
     * @param fields 字段
     * @return 数据映射：外层key为编号，内层key为字段名，value为字段值
     */
    Map<Integer, Map<String, Object>> all(Set<String> fields);

    /**
     * 查询数据是否存在
     * @param id 编号
     * @return 是否存在
     */
    boolean exist(Integer id);

    /**
     * 查询数据量
     * @return 数据量
     */
    int total();

    /**
     * 根据条件查询数据量
     * @param criteria 条件
     * @return 数据量
     */
    int count(@NonNull Criteria criteria);

    /**
     * 添加一条数据
     * @param entity 数据实体
     */
    void add(T entity);

    /**
     * 更新一条数据
     * @param entity 数据实体
     */
    void set(T entity);

    /**
     * 添加或更新一条数据
     * @param entity 数据实体
     */
    void put(T entity);

    /**
     * 删除一条数据
     * @param id 编号
     */
    void del(Integer id);

    /**
     * 根据条件删除数据
     * @param criteria 条件
     */
    void del(@NonNull Criteria criteria);

}
