package me.kujio.sprout.system.controller;

import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.system.entity.SysUser;
import me.kujio.sprout.system.service.SysUserService;
import me.kujio.sprout.utils.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping("/list")
    public JRst list(Query query) {
        return OK(sysUserService.list(query).stream().map(SysUser::secureGet).toList());
    }

    @GetMapping("/count")
    public JRst count(Query query) {
        return OK(sysUserService.count(query));
    }

    @GetMapping(value = "/{id}")
    public JRst get(@PathVariable Integer id) {
        return OK(SysUser.secureGet(sysUserService.get(id)));
    }

    @GetMapping("/all")
    public JRst all(@RequestParam List<String> fields) {
        fields.retainAll(SysUser.secureFields);
        return OK(sysUserService.all(fields));
    }

    @PutMapping
    public JRst put(@RequestBody SysUser sysUser) {
        sysUserService.put(SysUser.securePut(sysUser));
        return OK();
    }

    @DeleteMapping("/{id}")
    public JRst del(@PathVariable Integer id) {
        sysUserService.del(id);
        return OK();
    }

    @PostMapping("/upload-avatar")
    public JRst uploadAvatar(@RequestParam("file") MultipartFile file) {
        String fileName = "sys-user/avatar-" + System.nanoTime()+".png";
        return OK(FileUtils.upload(fileName, file));
    }
}
