package me.kujio.sprout.system.controller;

import me.kujio.sprout.core.annotation.Anonymous;
import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.LoginInfo;
import me.kujio.sprout.core.service.SysLoginService;
import me.kujio.sprout.system.entity.SysUser;
import org.springframework.web.bind.annotation.*;


import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
public class SysLoginController {
    private final SysLoginService sysLoginService;

    public SysLoginController(SysLoginService sysLoginService) {
        this.sysLoginService = sysLoginService;
    }

    @Anonymous
    @PostMapping("/login")
    public JRst login(@RequestBody LoginInfo loginInfo) {
        return OK(sysLoginService.login(loginInfo));
    }

    @PostMapping("/user-logout")
    public JRst logout() {
        sysLoginService.logout();
        return OK();
    }

    public record Password(String old, String fresh) {
    }

    @PostMapping("/change-password")
    public JRst changePassword(@RequestBody Password password) {
        sysLoginService.changePassword(password.old, password.fresh);
        return OK();
    }

    @PostMapping("/reset-password")
    public JRst resetPassword(@RequestBody String userName) {
        sysLoginService.resetPassword(userName);
        return OK();
    }

    @Anonymous
    @GetMapping("/captcha")
    public JRst captcha(){
        return OK(sysLoginService.captcha());
    }

    @Anonymous
    @GetMapping("/is-login")
    public JRst isLogin() {
        try {
            AuthInfo.loginUser();
            return OK(true);
        }catch (Exception e){
            return OK(false);
        }
    }

    @GetMapping("/login-info")
    public JRst loginUser(){
        return OK(SysUser.secureGet((SysUser) AuthInfo.loginUser()));
    }
}
