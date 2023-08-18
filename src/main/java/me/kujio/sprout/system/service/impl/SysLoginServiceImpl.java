package me.kujio.sprout.system.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.conifg.TokenConfig;
import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.entity.LoginInfo;
import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.core.service.AuthInfoService;
import me.kujio.sprout.core.service.SysLoginService;
import me.kujio.sprout.utils.CacheUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class SysLoginServiceImpl implements SysLoginService {

    private final AuthInfoService authInfoService;

    @Override
    public String login(LoginInfo loginInfo) {
        loginInfo.valid();
        UserDetails user = authInfoService.loadUserByUsername(loginInfo.getName());
        String md5 = SecureUtil.md5(loginInfo.getPassword());
        if (user == null || !user.getPassword().equals(md5)) {
            throw new SysException("用户名或密码错误");
        }
        String uuid = UUID.fastUUID().toString();
        String token = newToken(user.getUsername(), uuid);
        refreshToken(user.getUsername(), uuid);
        return token;
    }

    @Override
    public void logout() {
        AuthInfo authInfo = AuthInfo.loginUser();
        CacheUtils.del("authToken: " + authInfo.getUsername() + " uuid=" + authInfo.getUuid());
    }

    @Override
    public LoginInfo.Captcha captcha() {
        String key = String.valueOf(System.nanoTime());
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        CacheUtils.put("captcha:" + key, captcha.getCode(),TokenConfig.getValidity());
        return new LoginInfo.Captcha(key, captcha.getImageBase64Data());
    }

    @Override
    public String newToken(String userName, String uuid) {
        return "Bearer " + JWT.create()
                .setPayload("name", userName)
                .setPayload("uuid", uuid)
                .setKey(TokenConfig.getSecret().getBytes())
                .sign();
    }

    @Override
    public AuthInfo parseToken(HttpServletRequest request) {
        String token = request.getHeader(TokenConfig.getHeader());
        if (token == null || token.isBlank() || !token.startsWith("Bearer")) return null;
        JWT jwt;
        try {
            jwt = JWTUtil.parseToken(token.substring(7).trim());
        } catch (JWTException ignored) {
            return null;
        }
        jwt.setKey(TokenConfig.getSecret().getBytes());
        if (!jwt.verify()) return null;

        Object nameObj = jwt.getPayload("name");
        Object uuidObj = jwt.getPayload("uuid");
        if (nameObj == null || uuidObj == null) return null;
        String name = String.valueOf(nameObj);
        String uuid = String.valueOf(uuidObj);

        if (!CacheUtils.hasKey("authToken: " + name + " uuid=" + uuid)) return null;

        AuthInfo authInfo = (AuthInfo) authInfoService.loadUserByUsername(name);
        if (authInfo == null) return null;
        authInfo.setUuid(uuid);
        return authInfo;
    }

    @Override
    public void refreshToken(String name, String uuid) {
        long expires = TokenConfig.getValidity();
        CacheUtils.put("authToken: " + name + " uuid=" + uuid, System.currentTimeMillis(), expires);
    }

    @Override
    public void changePassword(String old, String fresh) {
        AuthInfo authInfo = AuthInfo.loginUser();
        if (!SecureUtil.md5(old).equals(authInfo.getPassword())) throw new SysException("旧密码错误");
        authInfoService.changePassword(authInfo, SecureUtil.md5(fresh));
        CacheUtils.delPrefix("authToken: " + authInfo.getUsername());
    }

    @Override
    public void resetPassword(String userName) {
        authInfoService.resetPassword(userName);
        CacheUtils.delPrefix("authToken: " + userName);
    }
}
