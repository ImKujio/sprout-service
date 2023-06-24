package me.kujio.sprout.system.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.kujio.sprout.base.entity.BaseEntity;
import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.dict.SysOwner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity implements AuthInfo {
    private String name;
    private String nickName;
    private String avatar;

    private String password;
    private String permissions;
    private LocalDateTime createTime;
    private Integer owner;
    private Set<Authority> authorities;
    @JSONField(serialize = false)
    private Map<String, String[]> parameterMap;
    @JSONField(serialize = false)
    private String uuid;


    @Override
    public Set<Authority> getAuthorities() {
        if (authorities != null) return authorities;
        if (permissions == null) return new HashSet<>();
        authorities = Arrays.stream(permissions.split(","))
                .filter(s -> !s.isBlank())
                .map(Authority::new).collect(Collectors.toSet());
        return authorities;
    }


    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return name;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean hasAuthority(String authority) {
        return getAuthorities().contains(new Authority(authority));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    @Override
    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }


    public static SysUser getSecure(SysUser user) {
        SysUser sysUser = new SysUser();
        sysUser.setId(user.getId());
        sysUser.setName(user.getName());
        sysUser.setNickName(user.getNickName());
        sysUser.setAvatar(user.getAvatar());
        sysUser.setCreateTime(user.getCreateTime());
        sysUser.setOwner(user.getOwner());
        return sysUser;
    }

    public static class Owner extends SysOwner {
    }

    @Component
    public static class Handle extends EntityHandle<SysUser> {
        {
            getter(SysUser::new);
            put("id", accessor(SysUser::getId, SysUser::setId));
            put("name", accessor(SysUser::getName, SysUser::setName));
            put("nickName", accessor(SysUser::getNickName, SysUser::setNickName));
            put("avatar",accessor(SysUser::getAvatar,SysUser::setAvatar));
            put("permissions", accessor(SysUser::getPermissions, SysUser::setPermissions));
            put("password", accessor(SysUser::getPassword, SysUser::setPassword));
            put("createTime", accessor(SysUser::getCreateTime, SysUser::setCreateTime));
            put("owner", accessor(SysUser::getOwner, SysUser::setOwner));
        }
    }

    public record Authority(String authority) implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return authority;
        }
    }

}
