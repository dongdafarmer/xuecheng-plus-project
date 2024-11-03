package com.xuecheng.base.exception;

import lombok.Data;

/**
 * @description 自定义异常类
 */

@Data
public class XueChengPlusException extends RuntimeException {
    String errMessage;

    public XueChengPlusException() {
        super();
    }

    public XueChengPlusException(String message) {
        super(message);
        this.errMessage = message;
    }

    public static void cast(String message) {
        throw new XueChengPlusException(message);
    }

    public static void cast(CommonError commonError) {
        throw new XueChengPlusException(commonError.getErrMessage());
    }
}
