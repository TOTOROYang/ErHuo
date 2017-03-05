package com.htu.erhuo.entity;

/**
 * Description
 * Created by yzw on 2017/3/5.
 */

public class EntityResponse<T> {
    // 统一返回码
    private String code;

    // 返回信息
    private T msg;

    /**
     * 错误状态返回结果
     *
     * @param code 返回码
     */
    public EntityResponse(String code) {
        this.code = code;
    }

    /**
     * 正确状态返回结果
     *
     * @param code 返回码
     * @param msg  返回信息
     */
    public EntityResponse(String code, T msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }
}

