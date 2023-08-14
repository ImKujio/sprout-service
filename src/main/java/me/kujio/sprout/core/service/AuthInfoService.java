package me.kujio.sprout.core.service;

import me.kujio.sprout.core.entity.AuthInfo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public interface AuthInfoService extends UserDetailsService {
    void changePassword(AuthInfo authInfo,String password);
    void resetPassword(String userName);
}
