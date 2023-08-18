package me.kujio.sprout.system.service;

import me.kujio.sprout.core.service.TableService;
import me.kujio.sprout.system.entity.SysMenu;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysMenuService extends TableService<SysMenu> {
    List<SysMenu> userMenus();
}
