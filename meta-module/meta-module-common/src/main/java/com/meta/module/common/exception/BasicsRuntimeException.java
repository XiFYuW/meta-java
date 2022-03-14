package com.meta.module.common.exception;

import com.meta.module.common.result.ResponseResult;

public class BasicsRuntimeException extends RuntimeException {

    private final ResponseResult responseResult;

    public BasicsRuntimeException(ResponseResult responseResult) {
        this.responseResult = responseResult;
    }

    public ResponseResult getResponseResult() {
        return responseResult;
    }
}
