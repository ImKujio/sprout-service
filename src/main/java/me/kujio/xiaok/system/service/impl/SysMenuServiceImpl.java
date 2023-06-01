package me.kujio.xiaok.system.service.impl;

import me.kujio.xiaok.base.entity.EntityHandle;
import me.kujio.xiaok.base.service.impl.BaseServiceImpl;
import me.kujio.xiaok.system.entity.SysMenu;
import me.kujio.xiaok.system.service.SysMenuService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements SysMenuService {

    public SysMenuServiceImpl(ApplicationContext context, EntityHandle<SysMenu> entityHandle) {
        super(context, entityHandle);
    }
}
