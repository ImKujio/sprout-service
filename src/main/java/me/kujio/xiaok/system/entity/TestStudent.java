package me.kujio.xiaok.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.kujio.xiaok.base.entity.EntityHandle;
import me.kujio.xiaok.base.entity.BaseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestStudent extends BaseEntity {
    private String name;
    private Integer age;
    private Boolean stay;
    private BigDecimal score;
    private LocalDate birthday;
    private LocalTime schoolDismissalTime;
    private LocalDateTime registrationTime;

    @Component
    public static class Handle extends EntityHandle<TestStudent> {
        {
            getter(TestStudent::new);
            put("id", accessor(TestStudent::getId, TestStudent::setId));
            put("name", accessor(TestStudent::getName, TestStudent::setName));
            put("age", accessor(TestStudent::getAge, TestStudent::setAge));
            put("stay", accessor(TestStudent::getStay, TestStudent::setStay));
            put("score", accessor(TestStudent::getScore, TestStudent::setScore));
            put("birthday", accessor(TestStudent::getBirthday, TestStudent::setBirthday));
            put("schoolDismissalTime", accessor(TestStudent::getSchoolDismissalTime, TestStudent::setSchoolDismissalTime));
            put("registrationTime", accessor(TestStudent::getRegistrationTime, TestStudent::setRegistrationTime));
        }
    }

}
