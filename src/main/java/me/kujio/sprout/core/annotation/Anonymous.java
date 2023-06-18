package me.kujio.sprout.core.annotation;

import java.lang.annotation.*;

/**
 * 匿名访问不鉴权注解
 * 
 */
@Target( ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Anonymous
{
}
