package me.kujio.xiaok.system.service.impl;

import me.kujio.xiaok.base.entity.EntityHandle;
import me.kujio.xiaok.base.service.impl.BaseServiceImpl;
import me.kujio.xiaok.system.entity.SysDict;
import me.kujio.xiaok.system.service.SysDictService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDict> implements SysDictService {

    public SysDictServiceImpl(ApplicationContext context, EntityHandle<SysDict> entityHandle) {
        super(context, entityHandle);
    }
}
