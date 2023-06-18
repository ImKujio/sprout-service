package me.kujio.sprout.system.controller;

import me.kujio.sprout.core.annotation.Anonymous;
import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.LoginInfo;
import me.kujio.sprout.core.service.SysLoginService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
@RequestMapping("/login")
public class SysLoginController {
    private final SysLoginService sysLoginService;

    public SysLoginController(SysLoginService sysLoginService) {
        this.sysLoginService = sysLoginService;
    }

    @Anonymous
    @PostMapping()
    public JRst login(@RequestBody LoginInfo loginInfo, HttpServletResponse response){
        return OK(sysLoginService.login(loginInfo,response));
    }

    @Anonymous
    @GetMapping("/captcha")
    public JRst captcha(){
        return OK(sysLoginService.captcha());
    }

}
