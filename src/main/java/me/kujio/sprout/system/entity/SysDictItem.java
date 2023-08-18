package me.kujio.sprout.system.entity;

import lombok.Data;
import me.kujio.sprout.core.table.Table;

@Data
@Table("sys_dict_item")
public class SysDictItem {
    private Integer id;
    private Integer dict;
    private String name;
    private String label;
    private String style;
    private String remark;

}