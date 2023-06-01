package me.kujio.xiaok.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.kujio.xiaok.base.entity.BaseEntity;
import me.kujio.xiaok.base.entity.EntityHandle;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictItem extends BaseEntity {
    private Integer dict;
    private String label;
    private String style;
    private String remark;

    @Component
    public static class Handle extends EntityHandle<SysDictItem> {
        {
            getter(SysDictItem::new);
            put("id", accessor(SysDictItem::getId, SysDictItem::setId));
            put("dict", accessor(SysDictItem::getDict, SysDictItem::setDict));
            put("label", accessor(SysDictItem::getLabel, SysDictItem::setLabel));
            put("style", accessor(SysDictItem::getStyle, SysDictItem::setStyle));
            put("remark", accessor(SysDictItem::getRemark, SysDictItem::setRemark));
        }
    }
}