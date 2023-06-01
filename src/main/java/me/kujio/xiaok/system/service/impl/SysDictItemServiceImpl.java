package me.kujio.xiaok.system.service.impl;

import me.kujio.xiaok.base.entity.EntityHandle;
import me.kujio.xiaok.base.service.impl.BaseServiceImpl;
import me.kujio.xiaok.system.entity.SysDictItem;
import me.kujio.xiaok.system.service.SysDictItemService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SysDictItemServiceImpl extends BaseServiceImpl<SysDictItem> implements SysDictItemService {

    public SysDictItemServiceImpl(ApplicationContext context, EntityHandle<SysDictItem> entityHandle) {
        super(context, entityHandle);
    }
}
