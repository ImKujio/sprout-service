package me.kujio.xiaok.core.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.kujio.xiaok.core.entity.JRst;
import me.kujio.xiaok.core.exception.SysException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int ACCESS_DENIED = 403;
    public static final int NOT_FOUND = 404;
    public static final int UNKNOWN = 410;
    public static final int SYSTEM = 411;

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public JRst handle(AccessDeniedException e,HttpServletRequest request){
        log.error("请求:{},权限不足:{}", request.getRequestURI(), e.getMessage());
        return JRst.ERR(ACCESS_DENIED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public JRst handle(IllegalArgumentException e,HttpServletRequest request) {
        log.error("请求:{},参数异常:{}", request.getRequestURI(), e.getMessage());
        return JRst.ERR(BAD_REQUEST,e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public JRst handle(MethodArgumentNotValidException e,HttpServletRequest request) {
        log.error("请求:{},参数异常:{}", request.getRequestURI(), e.getMessage());
        return JRst.ERR(BAD_REQUEST,e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(SysException.class)
    public JRst handle(SysException e,HttpServletRequest request) {
        log.error("请求:{},系统异常:{}", request.getRequestURI(), e.getMessage());
        return JRst.ERR(e.getCode(),e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public JRst handle(RuntimeException e, HttpServletRequest request) {
        log.error("请求:{},未知异常:{}", request.getRequestURI(), e.getMessage());
        e.printStackTrace();
        return JRst.ERR(UNKNOWN,e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public JRst handle(Exception e,HttpServletRequest request) {
        log.error("请求:{},未知异常:{}", request.getRequestURI(), e.getMessage());
        e.printStackTrace();
        return JRst.ERR(UNKNOWN,e.getMessage());
    }

}
