package me.kujio.sprout;

import me.kujio.sprout.core.entity.Query;
import me.kujio.sprout.system.entity.TestStudent;
import me.kujio.sprout.system.service.TestStudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
public class TableServiceTests {

    @Qualifier("testStudentServiceImpl")
    @Autowired
    private TestStudentService studentService;

    @Test
    void testNew(){
        long start = System.nanoTime();
        TestStudent student = new TestStudent(){{
            setId(8);
            setName("测1");
            setStay(true);
            setScore(BigDecimal.valueOf(99.99));
            setBirthday(LocalDate.now());
            setSchoolDismissalTime(LocalTime.now());
            setRegistrationTime(LocalDateTime.now());
        }};
        System.out.println("new time cost:"+(System.nanoTime() - start));
    }

    @Test
    void tesGet(){
        TestStudent student = studentService.get("id", 1);
        System.out.println(student);
    }

    @Test
    void testList(){
        studentService.list(Query.all());
    }

//    @Test
//    void testAdd(){
//        TestStudent testStudent = new TestStudent(){{
//           setName("测试");
//           setSex(2);
//           setStay(true);
//           setScore(BigDecimal.valueOf(99.99));
//           setBirthday(LocalDate.now());
//           setSchoolDismissalTime(LocalTime.now());
//           setRegistrationTime(LocalDateTime.now());
//        }};
//        studentService.add(testStudent);
//        System.out.println(testStudent.getId());
//    }

//    @Test
//    void testSet(){
//        TestStudent testStudent = new TestStudent(){{
//            setId(8);
//            setName("测1");
//            setSex(4);
//            setStay(true);
//            setScore(BigDecimal.valueOf(99.99));
//            setBirthday(LocalDate.now());
//            setSchoolDismissalTime(LocalTime.now());
//            setRegistrationTime(LocalDateTime.now());
//        }};
//        studentService.set(testStudent);
//    }

//    @Test
//    void testDel(){
//        studentService.del(9);
//    }
}
