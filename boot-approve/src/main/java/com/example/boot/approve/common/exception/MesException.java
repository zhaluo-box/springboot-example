package com.example.boot.approve.common.exception;

/**
 * Created  on 2022/3/10 10:10:27
 *
 * @author zl
 */
public class MesException extends RuntimeException {

    public MesException(String message) {
        super(message);
    }

    public MesException(String message, Throwable t) {
        super(message, t);
    }

    public MesException(Throwable t) {
        super(t);
    }

}
