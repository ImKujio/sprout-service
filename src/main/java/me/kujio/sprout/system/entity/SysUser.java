package me.kujio.sprout.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.kujio.sprout.base.entity.BaseEntity;
import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.dict.SysOwner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {
    private String name;
    private String nickName;
    private String password;
    private LocalDateTime createTime;
    private Integer owner;

    public static class Owner extends SysOwner {}

    @Component
    public static class Handle extends EntityHandle<SysUser> {
        {
            getter(SysUser::new);
            put("id", accessor(SysUser::getId, SysUser::setId));
            put("name", accessor(SysUser::getName, SysUser::setName));
            put("nickName", accessor(SysUser::getNickName, SysUser::setNickName));
            put("password", accessor(SysUser::getPassword, SysUser::setPassword));
            put("createTime", accessor(SysUser::getCreateTime, SysUser::setCreateTime));
            put("owner", accessor(SysUser::getOwner, SysUser::setOwner));
        }
    }

}
