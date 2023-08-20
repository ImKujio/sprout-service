package me.kujio.sprout.system.controller;

import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.system.entity.SysDictItem;
import me.kujio.sprout.system.service.SysDictItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
@RequestMapping("/sys-dict-item")
public class SysDictItemController {

    private final SysDictItemService sysDictItemService;

    public SysDictItemController(SysDictItemService sysDictItemService) {
        this.sysDictItemService = sysDictItemService;
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
