package me.kujio.sprout.core.exception;

import me.kujio.sprout.core.handler.ExceptionHandler;

public final class SysException extends RuntimeException{
    private final int code;

    public int getCode() {
        return code;
    }

    public SysException(String message) {
        super(message);
        this.code = ExceptionHandler.SYSTEM;
    }

    public SysException(int code, String message) {
        super(message);
        this.code = code;
    }
}
