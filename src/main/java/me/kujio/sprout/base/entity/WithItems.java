package me.kujio.sprout.base.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class WithItems<T extends BaseEntity, I extends BaseEntity> {
    private T data;
    private HashSet<I> items;

    public Integer dataId() {
        return data.getId();
    }

    /**
     * 将源数据与items对比，通过id字段和hash找出新增的执行addRunner；变更的执行setRunner；删除的执行delRunner。
     *
     * @param src       源数据
     * @param putRunner 变更的执行setRunner
     * @param delRunner 删除的执行delRunner
     */
    public void compare(List<I> src, PutRunner<I> putRunner, DelRunner<I> delRunner) {
        Map<Integer, I> srcMap = new HashMap<>();
        for (I item : src) {
            if (items.contains(item)) {
                items.remove(item);
            } else {
                srcMap.put(item.getId(), item);
            }
        }
        for (I item : items) {
            if (srcMap.containsKey(item.getId())) {
                srcMap.remove(item.getId());
                putRunner.run(item);
                continue;
            }
            item.setId(null);
            putRunner.run(item);
        }
        for (I item : srcMap.values()) {
            delRunner.run(item);
        }
    }

    public interface PutRunner<E> {
        void run(E putItem);
    }

    public interface DelRunner<E> {
        void run(E delItem);
    }

}
