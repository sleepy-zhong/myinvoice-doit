// com.example.myinvoice.utils.Result.java
package com.example.myinvoice.util.login;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        Result r = new Result();
        r.setCode(200);
        r.setMessage("登录成功");
        r.setData(data);
        return r;
    }

    public static Result error(String msg) {
        Result r = new Result();
        r.setCode(400);
        r.setMessage(msg);
        return r;
    }
}
