package me.kujio.sprout.system.controller;

import me.kujio.sprout.core.annotation.Anonymous;
import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.LoginInfo;
import me.kujio.sprout.core.service.SysLoginService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
public class SysLoginController {
    private final SysLoginService sysLoginService;

    public SysLoginController(SysLoginService sysLoginService) {
        this.sysLoginService = sysLoginService;
    }

    @Anonymous
    @PostMapping("/login")
    public JRst login(@RequestBody LoginInfo loginInfo, HttpServletResponse response){
        sysLoginService.login(loginInfo,response);
        return OK();
    }

    @PostMapping("/user-logout")
    public JRst logout(HttpServletResponse response){
        sysLoginService.logout(response);
        return OK();
    }

    @Anonymous
    @GetMapping("/captcha")
    public JRst captcha(){
        return OK(sysLoginService.captcha());
    }

    @Anonymous
    @GetMapping("/is-login")
    public JRst isLogin(){
        AuthInfo authInfo = AuthInfo.loginUser();
        return OK(authInfo != null);
    }
}
