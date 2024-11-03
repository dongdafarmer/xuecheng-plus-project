package com.xuecheng.base.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 和前端约定返回的异常信息模型
 */

@Data
public class RestErrorResponse implements Serializable {
    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }

}
