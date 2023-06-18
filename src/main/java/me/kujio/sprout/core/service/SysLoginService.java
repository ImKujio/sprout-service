package me.kujio.sprout.core.service;

import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.entity.LoginInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public interface SysLoginService {
    boolean login(LoginInfo loginInfo, HttpServletResponse response);

    LoginInfo.Captcha captcha();

    String newToken(AuthInfo user);

    AuthInfo parseToken(HttpServletRequest request, HttpServletResponse response);
}
