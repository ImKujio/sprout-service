package me.kujio.sprout.system.service.impl;

import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.service.TableServiceImpl;
import me.kujio.sprout.system.entity.SysDict;
import me.kujio.sprout.system.entity.SysDictItem;
import me.kujio.sprout.system.service.SysDictItemService;
import me.kujio.sprout.system.service.SysDictService;
import me.kujio.sprout.utils.CacheUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysDictServiceImpl extends TableServiceImpl<SysDict> implements SysDictService {

    private final SysDictItemService sysDictItemService;

    public SysDictServiceImpl(
            SysDictItemService sysDictItemService
    ) {
        this.sysDictItemService = sysDictItemService;
    }

    @Override
    public Map<String, Map<Integer, SysDictItem>> allDict() {
        return CacheUtils.getOrPut(sysDictItemService.cacheKey("allDict"), () -> {
            Map<Integer, SysDict> dictNameMap = dict(List.of("name"));
            Map<String, Map<Integer, SysDictItem>> allDict = new HashMap<>();
            List<SysDictItem> dictItems = sysDictItemService.list(Query.all());
            for (SysDictItem dictItem : dictItems) {
                String dictName = dictNameMap.get(dictItem.getDict()).getName();
                Map<Integer, SysDictItem> dict = allDict.computeIfAbsent(dictName, k -> new HashMap<>());
                dict.put(dictItem.getId(), dictItem);
            }
            return allDict;
        });
    }

    @Override
    public void putWithItems(SysDict sysDict, List<SysDictItem> items) {
        put(sysDict);
        items.forEach(item -> item.setDict(sysDict.getId()));
        sysDictItemService.put(items, "dict", sysDict.getId());
    }

    @Override
    public void clearCache() {
        super.clearCache();
        sysDictItemService.clearCache();
    }
}
