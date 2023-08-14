package me.kujio.sprout.core.service;

import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.entity.LoginInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public interface SysLoginService {

    String login(LoginInfo loginInfo);

    void logout();

    LoginInfo.Captcha captcha();

    String newToken(String user, String uuid);

    AuthInfo parseToken(HttpServletRequest request);

    void refreshToken(String name, String uuid);

    void changePassword(String old, String fresh);

    void resetPassword(String userName);
}
