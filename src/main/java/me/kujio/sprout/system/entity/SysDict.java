package me.kujio.sprout.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.kujio.sprout.base.entity.BaseEntity;
import me.kujio.sprout.base.entity.EntityHandle;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysDict extends BaseEntity {

    private String name;
    private String label;
    private String remark;
    private Integer owner;

    @Component
    public static class Handle extends EntityHandle<SysDict> {
        {
            getter(SysDict::new);
            put("id", accessor(SysDict::getId, SysDict::setId));
            put("name", accessor(SysDict::getName, SysDict::setName));
            put("label", accessor(SysDict::getLabel, SysDict::setLabel));
            put("remark", accessor(SysDict::getRemark, SysDict::setRemark));
            put("owner", accessor(SysDict::getOwner, SysDict::setOwner));
        }
    }

}
