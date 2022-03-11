package com.example.boot.approve.common.exception;

/**
 * Created  on 2022/3/10 10:10:27
 *
 * @author zl
 */
public class MESException extends RuntimeException {

    public MESException(String message) {
        super(message);
    }

    public MESException(String message, Throwable t) {
        super(message, t);
    }

    public MESException(Throwable t) {
        super(t);
    }

}
