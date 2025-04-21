package com.example.myinvoice.Entity.DTO;

// 在 com.example.myinvoice.Entity.DTO 包下创建 ApiResult.java
public class ApiResult<T> {
    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "success", data);
    }

    public static <T> ApiResult<T> error(int code, String message) {
        return new ApiResult<>(code, message, null);
    }

    // 构造函数和getter/setter
    public ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 省略getter和setter，根据你的项目实际情况添加
}