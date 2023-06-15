package me.kujio.sprout.system.service;

import me.kujio.sprout.base.entity.WithItems;
import me.kujio.sprout.base.service.BaseService;
import me.kujio.sprout.system.entity.SysDict;
import me.kujio.sprout.system.entity.SysDictItem;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public interface SysDictService extends BaseService<SysDict> {

    Map<String,Map<Integer, SysDictItem>> allDict();

    @Transactional
    void putWithItems(WithItems<SysDict,SysDictItem> withItems);

}
