package me.kujio.sprout.core.entity;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface AuthInfo extends UserDetails {

    boolean hasAuthority(String authority);

    Map<String,String[]> getParameterMap();

    void setParameterMap(Map<String,String[]> parameterMap);
}
