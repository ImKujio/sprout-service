package me.kujio.sprout.core.entity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface AuthInfo extends UserDetails {

    boolean hasAuthority(String authority);

    Map<String, String[]> getParameterMap();

    void setParameterMap(Map<String, String[]> parameterMap);

    String getUuid();

    void setUuid(String uuid);

    static AuthInfo loginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthInfo)
            return (AuthInfo) principal;
        else return null;
    }
}
