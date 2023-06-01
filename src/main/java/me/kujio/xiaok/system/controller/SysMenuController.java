package me.kujio.xiaok.system.controller;

import me.kujio.xiaok.base.entity.Query;
import me.kujio.xiaok.core.entity.JRst;
import me.kujio.xiaok.system.entity.SysMenu;
import me.kujio.xiaok.system.service.SysMenuService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static me.kujio.xiaok.core.entity.JRst.OK;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @GetMapping("/list")
    public JRst list(Query query) {
        return OK(sysMenuService.list(query));
    }

    @GetMapping("/total")
    public JRst total() {
        return OK(sysMenuService.total());
    }

    @GetMapping(value = "/{id}")
    public JRst get(@PathVariable Integer id) {
        return OK(sysMenuService.get(id));
    }

    @GetMapping("/all")
    public JRst all(@RequestParam Set<String> fields) {
        return OK(sysMenuService.all(fields));
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
