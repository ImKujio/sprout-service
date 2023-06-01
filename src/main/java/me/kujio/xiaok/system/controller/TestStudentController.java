package me.kujio.xiaok.system.controller;

import me.kujio.xiaok.base.entity.Query;
import me.kujio.xiaok.core.entity.JRst;
import me.kujio.xiaok.system.entity.TestStudent;
import me.kujio.xiaok.system.service.TestStudentService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static me.kujio.xiaok.core.entity.JRst.OK;

@RestController
@RequestMapping("/test/student")
public class TestStudentController {

    private final TestStudentService testStudentService;

    public TestStudentController(TestStudentService testStudentService) {
        this.testStudentService = testStudentService;
    }

    @GetMapping("/list")
    public JRst list(Query query) {
        return OK(testStudentService.list(query));
    }

    @GetMapping("/total")
    public JRst total() {
        return OK(testStudentService.total());
    }

    @GetMapping(value = "/{id}")
    public JRst get(@PathVariable Integer id) {
        return OK(testStudentService.get(id));
    }

    @GetMapping("/all")
    public JRst all(@RequestParam Set<String> fields) {
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
}
