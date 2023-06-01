package me.kujio.xiaok.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.kujio.xiaok.base.entity.BaseEntity;
import me.kujio.xiaok.base.entity.EntityHandle;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity {

    private Integer pid;
    private Integer type;
    private String name;
    private Integer sort;
    private String path;

    @Component
    public static class Handle extends EntityHandle<SysMenu> {
        {
            getter(SysMenu::new);
            put("id", accessor(SysMenu::getId, SysMenu::setId));
            put("pid", accessor(SysMenu::getPid, SysMenu::setPid));
            put("type", accessor(SysMenu::getType, SysMenu::setType));
            put("name", accessor(SysMenu::getName, SysMenu::setName));
            put("sort", accessor(SysMenu::getSort, SysMenu::setSort));
            put("path", accessor(SysMenu::getPath, SysMenu::setPath));
        }
    }

}
