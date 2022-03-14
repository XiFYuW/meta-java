package com.meta.module.common.result;

public class ResponseResultUtils {

    public static ResponseResult getResponseResultSUCCESS() {
        return new ResponseResult(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), new Object());
    }

    public static ResponseResult getResponseResultDataS(Object data) {
        return new ResponseResult(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data == null ? new Object() : data);
    }

    public static ResponseResult getResponseResultFAILED() {
        return new ResponseResult(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMessage(), new Object());
    }

    public static ResponseResult getResponseResultF(String msg) {
        return new ResponseResult(ResultEnum.FAILED.getCode(), msg, new Object());
    }

    public static ResponseResult getResponseResultS(String msg) {
        return new ResponseResult(ResultEnum.SUCCESS.getCode(), msg, new Object());
    }

    public static ResponseResult getResponseResult(int code, String msg, Object data) {
        return new ResponseResult(code, msg, data);
    }

    public static ResponseResult getResponseResultS(String msg, Object data) {
        return new ResponseResult(ResultEnum.SUCCESS.getCode(), msg, data);
    }

    public static ResponseResult getResponseResult(int code, String msg) {
        return new ResponseResult(code, msg, new Object());
    }

}
