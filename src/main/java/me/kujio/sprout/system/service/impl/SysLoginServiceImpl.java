package me.kujio.sprout.system.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.conifg.TokenConfig;
import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.exception.AuthException;
import me.kujio.sprout.core.exception.SysException;
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

@Service
@RequiredArgsConstructor
public class SysLoginServiceImpl implements SysLoginService {
    private final UserDetailsService userDetailsService;

    @Override
    public boolean login(LoginInfo loginInfo, HttpServletResponse response) {
        loginInfo.valid();
        UserDetails user = userDetailsService.loadUserByUsername(loginInfo.getName());
        String md5 = SecureUtil.md5(loginInfo.getPassword());
        if (user == null || !user.getPassword().equals(md5)){
            throw new SysException("用户名或密码错误");
        }
        AuthInfo authInfo = (AuthInfo) user;
        response.setHeader(TokenConfig.getHeader(), newToken(authInfo));
        return true;
    }

    @Override
    public LoginInfo.Captcha captcha() {
        String key = String.valueOf(System.nanoTime());
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        CacheUtils.put("captcha: " + key, captcha.getCode());
        return new LoginInfo.Captcha(key,captcha.getCode());
    }

    @Override
    public String newToken(AuthInfo user) {
        LocalDateTime expires = LocalDateTime.now().plusMinutes(TokenConfig.getValidity());
        return JWT.create()
                .setExpiresAt(TimeUtils.ldt2Date(expires))
                .setPayload("name", user.getUsername())
                .setKey(TokenConfig.getSecret().getBytes())
                .sign();
    }

    @Override
    public AuthInfo parseToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(TokenConfig.getHeader());
        if (token == null || token.isBlank()) return null;
        JWT jwt;
        try{
             jwt = JWTUtil.parseToken(token);
        }catch (JWTException ignored){
            return null;
        }
        jwt.setKey(TokenConfig.getSecret().getBytes());
        if (!jwt.validate(0L)) return null;
        Object nameObj = jwt.getPayload("name");
        if (nameObj == null) return null;
        String name = String.valueOf(nameObj);
        UserDetails user = userDetailsService.loadUserByUsername(name);
        if (user == null) return null;
        AuthInfo authInfo = (AuthInfo) user;
        response.setHeader(TokenConfig.getHeader(), newToken(authInfo));
        return authInfo;
    }
}
