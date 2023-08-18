package me.kujio.sprout.system.service;

import me.kujio.sprout.core.service.TableService;
import me.kujio.sprout.system.entity.SysDict;
import me.kujio.sprout.system.entity.SysDictItem;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public interface SysDictService extends TableService<SysDict> {

    Map<String,Map<Integer, SysDictItem>> allDict();

    @Transactional
    void putWithItems(SysDict sysDict, List<SysDictItem> items);

}
