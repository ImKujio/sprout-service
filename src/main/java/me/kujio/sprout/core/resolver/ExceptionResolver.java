package me.kujio.sprout.core.resolver;

import lombok.extern.slf4j.Slf4j;
import me.kujio.sprout.core.entity.JRst;
import me.kujio.sprout.core.exception.AuthException;
import me.kujio.sprout.core.exception.SysException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static me.kujio.sprout.core.exception.SysException.*;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionResolver implements AuthenticationEntryPoint {

    @ResponseBody
    @ExceptionHandler(AuthException.class)
    public JRst handle(AuthException e,HttpServletRequest request) {
        log.error("请求:{},{}", request.getRequestURI(), e.getMessage());
        return JRst.ERR(UN_AUTHORIZATION,e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public JRst handle(AccessDeniedException e,HttpServletRequest request) {
        log.error("请求:{},权限异常:{}", request.getRequestURI(), e.getMessage());
        return JRst.ERR(ACCESS_DENIED,"访问权限不足");
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public JRst handle(IllegalArgumentException e, HttpServletRequest request) {
        log.error("请求:{},参数异常:{}", request.getRequestURI(), e.getMessage());
        return JRst.ERR(BAD_REQUEST, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JRst handle(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("请求:{},参数异常:{}", request.getRequestURI(), e.getMessage());
        return JRst.ERR(BAD_REQUEST, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(SysException.class)
    public JRst handle(SysException e, HttpServletRequest request) {
        log.error("请求:{},系统异常:{}", request.getRequestURI(), e.getMessage());
        return JRst.ERR(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public JRst handle(RuntimeException e, HttpServletRequest request) {
        log.error("请求:{},未知异常:{}", request.getRequestURI(), e.getMessage());
        e.printStackTrace();
        return JRst.ERR(UNKNOWN, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public JRst handle(Exception e, HttpServletRequest request) {
        log.error("请求:{},未知异常:{}", request.getRequestURI(), e.getMessage());
        e.printStackTrace();
        return JRst.ERR(UNKNOWN, e.getMessage());
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("请求:{},认证异常:{}", request.getRequestURI(), authException.getMessage());
        JRst errorResponse;
        if (authException instanceof AuthException) {
            errorResponse = JRst.ERR(UN_AUTHORIZATION, authException.getMessage());
        } else {
            errorResponse = JRst.ERR(UN_AUTHORIZATION, "未登录");
        }
        errorResponse.writer(response);
    }

}
