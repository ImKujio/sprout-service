package me.kujio.sprout.system.controller;

import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.service.AuthorityService;
import me.kujio.sprout.system.entity.SysDictItem;
import me.kujio.sprout.system.service.SysDictItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sys-dict-item")
public class SysDictItemController {

    private final AuthorityService authorityService;
    private final SysDictItemService sysDictItemService;

    @PostConstruct
    public void registerAuthority() {
        authorityService.register(new AuthorityService.Register() {{

        }});
    }

    @GetMapping("/list")
    public JRst list(Query query) {
        return OK(sysDictItemService.list(query));
    }

    @GetMapping("/count")
    public JRst count(Query query) {
        return OK(sysDictItemService.count(query));
    }

    @GetMapping(value = "/{id}")
    public JRst get(@PathVariable Integer id) {
        return OK(sysDictItemService.get(id));
    }

    @GetMapping("/dict")
    public JRst dict(@RequestParam List<String> fields) {
        return OK(sysDictItemService.dict(fields));
    }

    @PutMapping
    public JRst put(@RequestBody SysDictItem sysDictItem) {
        sysDictItemService.put(sysDictItem);
        return OK();
    }

    @DeleteMapping("/{id}")
    public JRst del(@PathVariable Integer id) {
        sysDictItemService.del(id);
        return OK();
    }
}
