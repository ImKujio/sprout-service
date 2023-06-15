package me.kujio.sprout.system.service.impl;

import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.base.entity.Where;
import me.kujio.sprout.base.entity.WithItems;
import me.kujio.sprout.base.service.impl.BaseServiceImpl;
import me.kujio.sprout.system.entity.SysDict;
import me.kujio.sprout.system.entity.SysDictItem;
import me.kujio.sprout.system.service.SysDictItemService;
import me.kujio.sprout.system.service.SysDictService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDict> implements SysDictService {

    private final SysDictItemService sysDictItemService;

    private final String cacheKeyAllDict;

    public SysDictServiceImpl(
            ApplicationContext context,
            EntityHandle<SysDict> entityHandle,
            SysDictItemService sysDictItemService
    ) {
        super(context, entityHandle);
        this.sysDictItemService = sysDictItemService;

        // 当字典项更新时，删除allDict的缓存
        this.cacheKeyAllDict = entityHandle.entityName() + ": allDict";
        sysDictItemService.onUpdate(() -> {
            cacheUtils.del(cacheKeyAllDict);
        });
    }

    @Override
    public Map<String, Map<Integer, SysDictItem>> allDict() {
        return cacheUtils.getOrPut(cacheKeyAllDict, () -> {
            Map<Integer, Map<String, Object>> dictNameMap = all(Set.of("name"));
            Map<String, Map<Integer, SysDictItem>> allDict = new HashMap<>();
            List<SysDictItem> dictItems = sysDictItemService.list(Where.of());
            for (SysDictItem dictItem : dictItems) {
                String dictName = String.valueOf(allValue(dictNameMap, dictItem.getDict(), "name"));
                if (dictName.equals("null")) continue;
                Map<Integer, SysDictItem> dict = allDict.get(dictName);
                if (dict == null) {
                    dict = new HashMap<>();
                    allDict.put(dictName, dict);
                }
                dict.put(dictItem.getId(), dictItem);
            }
            return allDict;
        });
    }

    @Override
    public void putWithItems(WithItems<SysDict, SysDictItem> withItems) {
        put(withItems.getData());
        List<SysDictItem> dictItems = sysDictItemService.list(
                Where.of(Where.item("dict", "=", withItems.dataId()))
        );
        withItems.compare(
                dictItems,
                entity -> {
                    entity.setDict(withItems.dataId());
                    sysDictItemService.put(entity);
                },
                delItem -> sysDictItemService.del(delItem.getId())
        );
    }
}
