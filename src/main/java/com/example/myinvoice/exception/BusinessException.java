package com.example.myinvoice.exception;


import com.example.myinvoice.exception.enums.ErrorCodeEnum;

/**
 * 业务异常类，用于捕获自定义的业务错误
 */
public class BusinessException extends RuntimeException {
//    private final int code;
    private final ErrorCodeEnum errorCode;

//    public BusinessException(int code, String message) {
//        super(message);
//        this.code = code;
//    }
    public BusinessException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

//    public int getCode() {
//        return code;
//    }
    public ErrorCodeEnum getErrorCode() {
        return errorCode;
    }
}
