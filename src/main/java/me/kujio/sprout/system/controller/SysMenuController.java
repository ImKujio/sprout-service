package me.kujio.sprout.system.controller;

import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.service.AuthorityService;
import me.kujio.sprout.system.entity.SysMenu;
import me.kujio.sprout.system.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-menu")
public class SysMenuController {

    private final AuthorityService authorityService;
    private final SysMenuService sysMenuService;

    @PostConstruct
    public void registerAuthority() {
        authorityService.register(new AuthorityService.Register() {{

        }});
    }

    @GetMapping("/list")
    public JRst list(Query query) {
        return OK(sysMenuService.list(query));
    }

    @GetMapping("/user-menus")
    public JRst listUserMenus() {
        return OK(sysMenuService.userMenus());
    }

    @GetMapping("/count")
    public JRst count(Query query) {
        return OK(sysMenuService.count(query));
    }

    @GetMapping(value = "/{id}")
    public JRst get(@PathVariable Integer id) {
        return OK(sysMenuService.get(id));
    }

    @GetMapping("/dict")
    public JRst dict(@RequestParam List<String> fields) {
        return OK(sysMenuService.dict(fields));
    }

    @PutMapping
    public JRst put(@RequestBody SysMenu sysDict) {
        sysMenuService.put(sysDict);
        return OK();
    }

    @DeleteMapping("/{id}")
    public JRst del(@PathVariable Integer id) {
        sysMenuService.del(id);
        return OK();
    }
}
