package com.meta.module.common.exception;

import com.meta.module.common.result.ResponseResult;
import com.meta.module.common.result.ResponseResultUtils;

public class HintRuntimeException extends RuntimeException {

    private final String msg;

    public HintRuntimeException(String msg) {
        this.msg = msg;
    }

    public ResponseResult getMsg() {
        return ResponseResultUtils.getResponseResultF(this.msg);
    }
}
