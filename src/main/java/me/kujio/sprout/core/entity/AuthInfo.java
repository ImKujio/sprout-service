package me.kujio.sprout.core.entity;

import me.kujio.sprout.core.exception.SysException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface AuthInfo extends UserDetails {

    boolean hasAuthority(String authority);

    Map<String, String[]> getParameterMap();

    void setParameterMap(Map<String, String[]> parameterMap);

    String getUuid();

    void setUuid(String uuid);

    void setPassword(String password);

    static AuthInfo loginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthInfo)
            return (AuthInfo) principal;
        throw new SysException("认证失败");
    }
}
