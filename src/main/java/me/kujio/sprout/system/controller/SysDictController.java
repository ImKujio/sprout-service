package me.kujio.sprout.system.controller;

import me.kujio.sprout.base.entity.Query;
import me.kujio.sprout.base.entity.WithItems;
import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.service.AuthorityService;
import me.kujio.sprout.system.entity.SysDict;
import me.kujio.sprout.system.entity.SysDictItem;
import me.kujio.sprout.system.service.SysDictService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    private final SysDictService sysDictService;

    public SysDictController(SysDictService sysDictService, AuthorityService authorityService) {
        this.sysDictService = sysDictService;
        authorityService.register(new AuthorityService.Register(){{
            setDef("系统字典:查找列表");
            setDef("系统字典:查找字典");
        }});
    }

    @GetMapping("/list")
    @PreAuthorize("@authorityService.permit('系统字典:查找列表')")
    public JRst list(Query query) {
        return OK(sysDictService.list(query));
    }

    @GetMapping("/count")
    public JRst count(Query query) {
        return OK(sysDictService.count(query));
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("@authorityService.permit('系统字典:查找字典')")
    public JRst get(@PathVariable Integer id) {
        return OK(sysDictService.get(id));
    }

    @GetMapping("/all")
    public JRst all(@RequestParam Set<String> fields) {
        return OK(sysDictService.all(fields));
    }

    @PutMapping
    public JRst put(@RequestBody SysDict sysDict) {
        sysDictService.put(sysDict);
        return OK();
    }

    @PutMapping("/with-items")
    public JRst putWithItems(@RequestBody WithItems<SysDict, SysDictItem> withItems) {
        sysDictService.putWithItems(withItems);
        return OK();
    }

    @DeleteMapping("/{id}")
    public JRst del(@PathVariable Integer id) {
        sysDictService.del(id);
        return OK();
    }

    @GetMapping("/all-dict")
    public JRst allDict(){
        return OK(sysDictService.allDict());
    }
}
