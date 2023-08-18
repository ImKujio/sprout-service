package me.kujio.sprout.system.controller;

import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.system.entity.TestStudent;
import me.kujio.sprout.system.service.TestStudentService;
import me.kujio.sprout.utils.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static me.kujio.sprout.core.entity.JRst.OK;

@RestController
@RequestMapping("/test-student")
public class TestStudentController {

    private final TestStudentService testStudentService;

    public TestStudentController(TestStudentService testStudentService) {
        this.testStudentService = testStudentService;
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

    @GetMapping("/all")
    public JRst all(@RequestParam List<String> fields) {
        return OK(testStudentService.all(fields));
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
