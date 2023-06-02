package me.kujio.sprout.system.service.impl;

import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.base.service.impl.BaseServiceImpl;
import me.kujio.sprout.system.entity.SysDict;
import me.kujio.sprout.system.service.SysDictService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDict> implements SysDictService {

    public SysDictServiceImpl(ApplicationContext context, EntityHandle<SysDict> entityHandle) {
        super(context, entityHandle);
    }

}
