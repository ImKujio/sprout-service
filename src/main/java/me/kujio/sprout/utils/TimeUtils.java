package me.kujio.sprout.utils;

import java.sql.Time;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class TimeUtils {

    /**
     * LocalDateTime 转为 Date
     * @param date LocalDateTime
     * @return Date
     */
    public static Date ldt2Date(LocalDateTime date){
        if (date == null) return null;
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转为 LocalDateTime
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime date2Ldt(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Date 转为 LocalDate
     * @param date Date
     * @return LocalDate
     */
    public static LocalDate date2Ld(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date 转为 LocalDate
     * @param date Date
     * @return LocalDate
     */
    public static LocalDate date2Ld(java.sql.Date date) {
        if (date == null) return null;
        return date.toLocalDate();
    }

    /**
     * LocalDate 转为 Date
     * @param date LocalDate
     * @return Date
     */
    public static Date ld2Date(LocalDate date) {
        if (date == null) return null;
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转为 LocalTime
     * @param date Date
     * @return LocalTime
     */
    public static LocalTime date2Lt(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * LocalTime 转为 Date
     * @param time LocalTime
     * @return Date
     */
    public static Date lt2Date(LocalTime time) {
        if (time == null) return null;
        return Date.from(time.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Time 转为 LocalTime
     * @param time Date
     * @return LocalTime
     */
    public static LocalTime time2Lt(Time time) {
        if (time == null) return null;
        return time.toLocalTime();
    }

    /**
     * LocalTime 转为 Time
     * @param time LocalTime
     * @return Time
     */
    public static Time lt2Time(LocalTime time) {
        if (time == null) return null;
        return Time.valueOf(time);
    }
}
