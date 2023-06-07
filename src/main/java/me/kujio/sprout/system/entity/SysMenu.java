package me.kujio.sprout.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.kujio.sprout.base.entity.BaseEntity;
import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.dict.AdminMenuType;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity {

    private Integer pid;
    private Integer type;
    private String name;
    private String icon;
    private Integer sort;
    private String path;
    private String component;

    public static class Type extends AdminMenuType {
    }

    @Component
    public static class Handle extends EntityHandle<SysMenu> {
        {
            getter(SysMenu::new);
            put("id", accessor(SysMenu::getId, SysMenu::setId));
            put("pid", accessor(SysMenu::getPid, SysMenu::setPid));
            put("type", accessor(SysMenu::getType, SysMenu::setType));
            put("name", accessor(SysMenu::getName, SysMenu::setName));
            put("icon", accessor(SysMenu::getIcon, SysMenu::setIcon));
            put("sort", accessor(SysMenu::getSort, SysMenu::setSort));
            put("path", accessor(SysMenu::getPath, SysMenu::setPath));
            put("component", accessor(SysMenu::getComponent, SysMenu::setComponent));
        }
    }

}
