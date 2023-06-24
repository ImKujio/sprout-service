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
import me.kujio.sprout.core.exception.AuthException;
import me.kujio.sprout.core.entity.LoginInfo;
import me.kujio.sprout.core.service.SysLoginService;
import me.kujio.sprout.utils.CacheUtils;
import me.kujio.sprout.utils.TimeUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SysLoginServiceImpl implements SysLoginService {

    private final UserDetailsService userDetailsService;

    @Override
    public void login(LoginInfo loginInfo, HttpServletResponse response) {
        loginInfo.valid();
        UserDetails user = userDetailsService.loadUserByUsername(loginInfo.getName());
        String md5 = SecureUtil.md5(loginInfo.getPassword());
        if (user == null || !user.getPassword().equals(md5)) {
            throw new AuthException("用户名或密码错误");
        }
        AuthInfo authInfo = (AuthInfo) user;
        authInfo.setUuid(UUID.fastUUID().toString());
        response.setHeader(TokenConfig.getHeader(), newToken(authInfo));
        Set<String> uuids =  CacheUtils.getOrPut("loginUsers: " + authInfo.getUsername(), HashSet::new);
        uuids.add(authInfo.getUuid());
        CacheUtils.put("loginUsers: " + authInfo.getUsername(),uuids);
    }

    @Override
    public void logout(HttpServletResponse response) {
        AuthInfo authInfo = AuthInfo.loginUser();
        Set<String> uuids = CacheUtils.getOrPut("loginUsers: " + authInfo.getUsername(),HashSet::new);
        uuids.remove(authInfo.getUuid());
        CacheUtils.put("loginUsers: " + authInfo.getUsername(),uuids);
        response.setHeader(TokenConfig.getHeader(),"");
    }

    @Override
    public LoginInfo.Captcha captcha() {
        String key = String.valueOf(System.nanoTime());
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        CacheUtils.put("captcha: " + key, captcha.getCode(),TokenConfig.getValidity());
        return new LoginInfo.Captcha(key, captcha.getImageBase64Data());
    }

    @Override
    public String newToken(AuthInfo user) {
        LocalDateTime expires = LocalDateTime.now().plusMinutes(TokenConfig.getValidity());
        return "Bearer " + JWT.create()
                .setExpiresAt(TimeUtils.ldt2Date(expires))
                .setPayload("name", user.getUsername())
                .setPayload("uuid", user.getUuid())
                .setKey(TokenConfig.getSecret().getBytes())
                .sign();
    }

    @Override
    public AuthInfo parseToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(TokenConfig.getHeader());
        if (token == null || token.isBlank() || !token.startsWith("Bearer")) return null;
        JWT jwt;
        try {
            jwt = JWTUtil.parseToken(token.substring(7).trim());
        } catch (JWTException ignored) {
            return null;
        }
        jwt.setKey(TokenConfig.getSecret().getBytes());
        if (!jwt.validate(0L)) return null;
        Object nameObj = jwt.getPayload("name");
        Object uuidObj = jwt.getPayload("uuid");
        if (nameObj == null || uuidObj == null) return null;
        String name = String.valueOf(nameObj);
        String uuid = String.valueOf(uuidObj);
        UserDetails user = userDetailsService.loadUserByUsername(name);
        if (user == null) return null;
        AuthInfo authInfo = (AuthInfo) user;
        authInfo.setUuid(uuid);
        Set<String> uuids = CacheUtils.getOrPut("loginUsers: " + authInfo.getUsername(),HashSet::new);
        if (!uuids.contains(uuid)) return null;
        return authInfo;
    }

    @Override
    public void refreshToken(AuthInfo user, HttpServletResponse response) {
        response.setHeader(TokenConfig.getHeader(), newToken(user));
    }
}
