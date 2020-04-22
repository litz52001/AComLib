package com.test.testlib.net.module1.api.http.exception;

/**
 * Description: 服务端的异常处理类，根据与服务端约定的code判断
 */
public class ApiException extends RuntimeException {

    private int errorCode;
    private String message;

    public ApiException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.message = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
