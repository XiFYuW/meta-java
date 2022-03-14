package com.meta.module.common.result;

public enum ResultEnum {
    TOKEN_EXPIRED("token失效，请重新登录", 50014),

    TOKEN_ERR("token错误", 50013),

    TOKEN_MISS("token失踪", 500404),

    TOKEN_FALSIFY("token被篡改", 50066),

    FAILED("操作失败", 5000),

    SUCCESS("操作成功", 2000),

    FAILED_SP("没有设置二级密码", 40010),

    ;

    String message;

    int code;

    ResultEnum(String msg, int code) {
        this.message = msg;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
