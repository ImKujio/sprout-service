package me.kujio.sprout.system.controller;

import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.core.service.AuthorityService;
import me.kujio.sprout.system.entity.TestStudent;
import me.kujio.sprout.system.service.TestStudentService;
import me.kujio.sprout.utils.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.List;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test-student")
public class TestStudentController {

    private final AuthorityService authorityService;
    private final TestStudentService testStudentService;

    @PostConstruct
    public void registerAuthority() {
        authorityService.register(new AuthorityService.Register() {{

        }});
    }

    @GetMapping("/list")
    public JRst list(Query query) {
        return OK(testStudentService.list(query));
    }

    @GetMapping("/count")
    public JRst count(Query query) {
        return OK(testStudentService.count(query));
    }

    @GetMapping(value = "/{id}")
    public JRst get(@PathVariable Integer id) {
        return OK(testStudentService.get(id));
    }

    @GetMapping("/dict")
    public JRst dict(@RequestParam List<String> fields) {
        return OK(testStudentService.dict(fields));
    }

    @PutMapping
    public JRst put(@RequestBody TestStudent testStudent) {
        testStudentService.put(testStudent);
        return OK();
    }

    @DeleteMapping("/{id}")
    public JRst del(@PathVariable Integer id) {
        testStudentService.del(id);
        return OK();
    }

    @PostMapping("/upload-avatar")
    public JRst uploadAvatar(@RequestParam("file") MultipartFile file) {
        String fileName = "test-student/avatar-" + System.nanoTime()+".png";
        return OK(FileUtils.upload(fileName, file));
    }
}
