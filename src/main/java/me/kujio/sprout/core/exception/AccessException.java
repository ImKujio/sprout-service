package me.kujio.sprout.core.exception;

import org.springframework.security.access.AccessDeniedException;

public class AccessException extends AccessDeniedException {
    public AccessException(String msg) {
        super(msg);
    }
}
