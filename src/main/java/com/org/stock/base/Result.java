package com.org.stock.base;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.org.stock.constant.ResultCode;

/**
 * 通用返回对象
 */
public class Result<T> {
    private short code;
    private String msg;
    private T body;

    protected Result() {
    }

    protected Result(short code, String msg, T body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.code == ResultCode.SUCCESS;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, "成功", data);
    }

    /**
     * 成功返回结果
     *
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, "成功", null);
    }


    /**
     * 失败返回结果
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public static <T> Result<T> failed(short errorCode, String message) {
        return new Result<T>(errorCode, message, null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> failed(String message) {
        return new Result<T>(ResultCode.FAILED, message, null);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<T>(ResultCode.PARAMS_ERROR, message, null);
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
