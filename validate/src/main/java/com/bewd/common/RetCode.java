package com.bewd.common;

/**
 * 返回状态码
 */
public enum RetCode {
    OK(0, "操作成功"),

    FAILED(-1, "操作失败"),

    EXCEPTION(-2, "服务器异常，请联系系统管理员"),

    ACCESS_DENIED(-3, "当前用户无此权限"),

    TOKEN_OVERTIME(-4, "登录凭证已失效,请重新登录"),



    ;

    public int code;
    public String msg;

    RetCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
