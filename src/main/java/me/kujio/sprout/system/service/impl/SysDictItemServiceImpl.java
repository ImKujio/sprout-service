package me.kujio.sprout.system.service.impl;

import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.base.service.impl.BaseServiceImpl;
import me.kujio.sprout.system.entity.SysDictItem;
import me.kujio.sprout.system.service.SysDictItemService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SysDictItemServiceImpl extends BaseServiceImpl<SysDictItem> implements SysDictItemService {

    public SysDictItemServiceImpl(ApplicationContext context, EntityHandle<SysDictItem> entityHandle) {
        super(context, entityHandle);
    }
}
