package me.kujio.sprout.system.service.impl;

import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.base.entity.Query;
import me.kujio.sprout.base.service.impl.BaseServiceImpl;
import me.kujio.sprout.system.entity.SysMenu;
import me.kujio.sprout.system.service.SysMenuService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements SysMenuService {

    public SysMenuServiceImpl(ApplicationContext context, EntityHandle<SysMenu> entityHandle) {
        super(context, entityHandle);
    }

    @Override
    public List<SysMenu> userMenus() {
        String cacheKey = entityHandle.entityName() + " : userMenus";
        return cacheUtils.getOrPut(cacheKey, () -> list(Query.all()));
    }
}
