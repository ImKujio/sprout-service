package me.kujio.sprout.core.entity;

import com.alibaba.fastjson2.JSON;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class JRst implements Serializable {

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
        return JSON.toJSONString(this);
    }

    public byte[] bytes() {
        return JSON.toJSONBytes(this);
    }
}
