package me.kujio.sprout.system.controller;

import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.service.AuthorityService;
import me.kujio.sprout.system.entity.SysDict;
import me.kujio.sprout.system.entity.SysDictItem;
import me.kujio.sprout.system.service.SysDictService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-dict")
public class SysDictController {

    private final AuthorityService authorityService;
    private final SysDictService sysDictService;

    @PostConstruct
    public void registerAuthority() {
        authorityService.register(new AuthorityService.Register() {{

        }});
    }

    @GetMapping("/list")
    public JRst list(Query query) {
        return OK(sysDictService.list(query));
    }

    @GetMapping("/count")
    public JRst count(Query query) {
        return OK(sysDictService.count(query));
    }

    @GetMapping(value = "/{id}")
    public JRst get(@PathVariable Integer id) {
        return OK(sysDictService.get(id));
    }

    @GetMapping("/dict")
    public JRst dict(@RequestParam List<String> fields) {
        return OK(sysDictService.dict(fields));
    }

    @PutMapping
    public JRst put(@RequestBody SysDict sysDict) {
        sysDictService.put(sysDict);
        return OK();
    }

    public record WithItems(SysDict data, List<SysDictItem> items) {
    }

    @PutMapping("/with-items")
    public JRst putWithItems(@RequestBody WithItems withItems) {
        sysDictService.putWithItems(withItems.data, withItems.items);
        return OK();
    }

    @DeleteMapping("/{id}")
    public JRst del(@PathVariable Integer id) {
        sysDictService.del(id);
        return OK();
    }

    @GetMapping("/all-dict")
    public JRst allDict() {
        return OK(sysDictService.allDict());
    }
}
