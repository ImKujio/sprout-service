package me.kujio.sprout.system.entity;

import lombok.Data;
import me.kujio.sprout.core.table.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 测试学生
 */
@Data
@Table

public class TestStudent {
    /** 编号 */
    private Integer id;
    private String name;
    private String avatar;
    private Integer age;
    private Integer sex;
    private Boolean stay;
    private BigDecimal score;
    private LocalDate birthday;
    private LocalTime schoolDismissalTime;
    private LocalDateTime registrationTime;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
