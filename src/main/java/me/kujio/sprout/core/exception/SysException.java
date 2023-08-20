package me.kujio.sprout.core.exception;


public final class SysException extends RuntimeException{
    public static final int BAD_REQUEST = 400;
    public static final int UN_AUTHORIZATION = 401;
    public static final int ACCESS_DENIED = 403;
    public static final int NOT_FOUND = 404;
    public static final int UNKNOWN = 410;
    public static final int SYSTEM = 1000;

    private final int code;

    public int getCode() {
        return code;
    }

    public SysException(String message) {
        this(message,null);
    }

    public SysException(String message,Throwable cause){
        this(SysException.SYSTEM,message,cause);
    }

    public SysException(int code, String message) {
        this(code,message,null);
    }

    public SysException(int code,String message,Throwable cause){
        super(message,cause);
        this.code = code;
    }

}
