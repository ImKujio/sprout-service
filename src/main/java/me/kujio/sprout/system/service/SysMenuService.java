package me.kujio.sprout.system.service;

import me.kujio.sprout.base.service.BaseService;
import me.kujio.sprout.system.entity.SysMenu;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysMenuService extends BaseService<SysMenu> {
    List<SysMenu> userMenus();
}
