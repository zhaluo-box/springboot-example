package com.example.boot.approve.config;

import com.example.boot.approve.common.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 默认的MVC异常处理器
 * Created  on 2022/3/10 10:10:27
 *
 * @author zl
 */
@Slf4j
@ControllerAdvice
public class DefaultMvcExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ResourceConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handle(ResourceConflictException exception) {
        log.warn(ExceptionUtils.getMessage(exception), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ExceptionUtils.getMessage(exception));
    }

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handle(ResourceNotFoundException exception) {
        log.warn(ExceptionUtils.getMessage(exception), exception);
        return buildResultMap(HttpStatus.NOT_FOUND, ExceptionUtils.getMessage(exception));
    }

    @ResponseBody
    @ExceptionHandler(MesException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handle(MesException exception) {
        log.warn(ExceptionUtils.getMessage(exception), exception);
        return buildResultMap(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getMessage(exception));
    }

    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handle(BadRequestException exception) {
        log.warn(ExceptionUtils.getMessage(exception), exception);
        return buildResultMap(HttpStatus.BAD_REQUEST, ExceptionUtils.getMessage(exception));
    }

    @ResponseBody
    @ExceptionHandler(ResourceGoneException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ResponseEntity<String> handle(ResourceGoneException exception) {
        log.warn(ExceptionUtils.getMessage(exception), exception);
        return buildResultMap(HttpStatus.GONE, ExceptionUtils.getMessage(exception));
    }

    @ResponseBody
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handle(ForbiddenException exception) {
        log.warn(ExceptionUtils.getMessage(exception), exception);
        return buildResultMap(HttpStatus.FORBIDDEN, ExceptionUtils.getMessage(exception));
    }

    @ResponseBody
    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handle(ResourceAccessException exception) {
        log.warn(ExceptionUtils.getMessage(exception), exception);
        return buildResultMap(HttpStatus.UNAUTHORIZED, ExceptionUtils.getMessage(exception));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handle(Exception exception) {
        log.warn(ExceptionUtils.getMessage(exception), exception);
        return buildResultMap(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionUtils.getMessage(exception));
    }

    private ResponseEntity<String> buildResultMap(HttpStatus status, String errorMessage) {
        return ResponseEntity.status(status).body(errorMessage);
    }

}
