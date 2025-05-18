package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //自定义异常
    @ExceptionHandler(XueChengPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(XueChengPlusException e){
        //记录异常
        log.error("系统异常{}", e.getErrMessage(), e);

        //解析出异常信息
        String message = e.getErrMessage();
        return new RestErrorResponse(message);
    }

    //参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e){
        List<String> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().stream().forEach(FieldError -> errors.add(FieldError.getDefaultMessage()));

        //将list中的错误信息拼接
        String errorMessage = StringUtils.join(errors, ",");

        //记录异常
        log.error("系统异常{}", e.getMessage(), e);

        //解析出异常信息
        return new RestErrorResponse(errorMessage);
    }

    //系统异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e){
        //记录异常
        log.error("系统异常{}", e.getMessage(), e);

        //解析出异常信息
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }
}
