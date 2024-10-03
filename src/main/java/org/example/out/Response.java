package org.example.out;


import lombok.Data;

/**
 * @ClassName Response
 * @Description TODO
 * @Date 2022/11/2 10:04 上午
 * @Version 1.0
 */
@Data
public class Response<T> {
    private static final int SUCCESS_CODE = 0;
    private static final String SUCCESS_MSG = "成功";
    private int code;
    private String msg;
    private T data;
    private String serverTime;

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.setServerTime(String.valueOf(System.currentTimeMillis()));
    }


    public static <T> Response<T> success() {
        return new Response(SUCCESS_CODE, SUCCESS_MSG);
    }

    public static <T> Response<T> success(T data) {
        return new Response(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> Response<T> fail(int code, String msg) {
        return new Response(code, msg);
    }


    public Boolean isSuccess() {
        return this.getCode() == SUCCESS_CODE;
    }






}
