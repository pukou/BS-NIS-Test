package com.bsoft.nis.pojo.exchange;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 枚举请求响应
 * Created by Administrator on 2016/10/11.
 */
public enum ErrorFlag {
    /**
     * 授权失败
     */

    unauthorization("授权失败"),

    /**
     * 认证失败
     */
    authenticationFail("认证失败"),

    /**
     * token 失效
     */
    tokenInvalid("token 失效"),

    /**
     * 请求参数非法或存在参数为空等错误
     */
    illegalParam("请求参数非法或存在参数为空等错误"),

    /**
     * 服务端数据库操作失败
     */
    dbFail("服务端操作失败");

    private String desc;

    public String getDesc() {
        return desc;
    }

    ErrorFlag(String desc) {
        this.desc = desc;
    }

    @JsonValue
    public int getOrdinal() {
        return this.ordinal();
    }
}
