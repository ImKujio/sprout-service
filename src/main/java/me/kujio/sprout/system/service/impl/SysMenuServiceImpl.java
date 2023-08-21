package me.kujio.sprout.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.service.TableServiceImpl;
import me.kujio.sprout.system.entity.SysMenu;
import me.kujio.sprout.system.service.SysMenuService;
import me.kujio.sprout.utils.CacheUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends TableServiceImpl<SysMenu> implements SysMenuService {


    @Override
    public List<SysMenu> userMenus() {
        return CacheUtils.getOrPut(cacheKey("userMenus"),
                () -> list(Query.all()));
    }
}
