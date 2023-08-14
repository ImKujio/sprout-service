package me.kujio.sprout.core.entity;

import lombok.Data;
import me.kujio.sprout.core.exception.AuthException;
import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.utils.CacheUtils;

@Data
public class LoginInfo {
    private String name;
    private String password;
    private String key;
    private String captcha;

    public void valid() {
        if (name == null || password == null || key == null || captcha == null)
            throw new SysException("登录信息不能为空");
        if (name.isBlank() || password.isBlank() || key.isBlank() || captcha.isBlank())
            throw new SysException("登录信息不能为空");
        String code = CacheUtils.get("captcha: " + key);
        CacheUtils.del("captcha: " + key);
        if (!captcha.equals(code)) throw new SysException("验证码错误");
    }

    public record Captcha(String key, String img) {
    }

}

