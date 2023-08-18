package me.kujio.sprout.system.entity;

import lombok.Data;
import me.kujio.sprout.core.table.Table;
import me.kujio.sprout.dict.SysOwner;

@Data
@Table("sys_dict")
public class SysDict {
    private Integer id;
    private String name;
    private String label;
    private String remark;
    private Integer owner;

    public static class Owner extends SysOwner {
    }

}
