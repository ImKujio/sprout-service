package me.kujio.sprout.system.entity;

import lombok.Data;
import me.kujio.sprout.core.table.Table;
import me.kujio.sprout.dict.AdminMenuType;

@Data
@Table("sys_menu")
public class SysMenu {

    private Integer id;
    private Integer pid;
    private Integer type;
    private String name;
    private String icon;
    private Integer sort;
    private String path;
    private String component;

    public static class Type extends AdminMenuType {
    }

}
