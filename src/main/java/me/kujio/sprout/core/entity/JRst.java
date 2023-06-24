package me.kujio.sprout.core.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import me.kujio.sprout.core.exception.SysException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class JRst implements Serializable {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Serial
    private static final long serialVersionUID = 1L;

    private final int code;
    private final String msg;
    private final Object data;

    private JRst(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static interface DataGetter {
        void get(Map<String, Object> map);
    }

    public static JRst OK() {
        return new JRst(200, "OK", null);
    }

    public static JRst OK(Object data) {
        return new JRst(200, "OK", data);
    }

    public static JRst OK(DataGetter getter) {
        Map<String, Object> map = new HashMap<>();
        getter.get(map);
        return new JRst(200, "OK", map);
    }

    public static JRst ERR(int code, String msg) {
        return new JRst(code, msg, null);
    }

    public String json() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new SysException(e.getMessage());
        }
    }

    public byte[] bytes() {
        try {
            return objectMapper.writeValueAsBytes(this);
        } catch (JsonProcessingException e) {
            throw new SysException(e.getMessage());
        }
    }

    public void writer(HttpServletResponse response){
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().print(json());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
