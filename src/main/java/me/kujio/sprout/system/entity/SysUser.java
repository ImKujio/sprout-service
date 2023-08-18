package me.kujio.sprout.system.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.table.Table;
import me.kujio.sprout.dict.SysOwner;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Table("sys_user")
public class SysUser implements AuthInfo {
    private Integer id;
    private String name;
    private String nickName;
    private String avatar;

    private String password;
    private String permissions;
    private LocalDateTime createTime;
    private Integer owner;
    @Transient
    private Set<Authority> authorities;
    @Transient
    @JSONField(serialize = false)
    private Map<String, String[]> parameterMap;
    @Transient
    @JSONField(serialize = false)
    private String uuid;

    public static class Owner extends SysOwner {
    }

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


    public static SysUser secureGet(SysUser user) {
        SysUser sysUser = new SysUser();
        sysUser.setId(user.getId());
        sysUser.setName(user.getName());
        sysUser.setNickName(user.getNickName());
        sysUser.setAvatar(user.getAvatar());
        sysUser.setPermissions(user.getPermissions());
        sysUser.setCreateTime(user.getCreateTime());
        sysUser.setOwner(user.getOwner());
        return sysUser;
    }

    public static SysUser securePut(SysUser user) {
        if (user == null) return null;
        SysUser sysUser = new SysUser();
        sysUser.setId(user.getId());
        sysUser.setName(user.getName());
        sysUser.setNickName(user.getNickName());
        sysUser.setAvatar(user.getAvatar());
        return sysUser;
    }

    public static Set<String> secureFields = Set.of(
            "id", "name", "nickName", "avatar", "createTime", "owner", "permissions"
    );

    public record Authority(String authority) implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return authority;
        }
    }

}
