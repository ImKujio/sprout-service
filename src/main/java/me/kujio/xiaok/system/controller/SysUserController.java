package me.kujio.xiaok.system.controller;

import me.kujio.xiaok.base.entity.Query;
import me.kujio.xiaok.core.entity.JRst;
import me.kujio.xiaok.system.entity.SysUser;
import me.kujio.xiaok.system.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static me.kujio.xiaok.core.entity.JRst.OK;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping("/list")
    public JRst list(Query query) {
        return OK(sysUserService.list(query));
    }

    @GetMapping("/total")
    public JRst total() {
        return OK(sysUserService.total());
    }

    @GetMapping(value = "/{id}")
    public JRst get(@PathVariable Integer id) {
        return OK(sysUserService.get(id));
    }

    @GetMapping("/all")
    public JRst all(@RequestParam Set<String> fields) {
        return OK(sysUserService.all(fields));
    }

    @PutMapping
    public JRst put(@RequestBody SysUser sysUser) {
        sysUserService.put(sysUser);
        return OK();
    }

    @DeleteMapping("/{id}")
    public JRst del(@PathVariable Integer id) {
        sysUserService.del(id);
        return OK();
    }
}
