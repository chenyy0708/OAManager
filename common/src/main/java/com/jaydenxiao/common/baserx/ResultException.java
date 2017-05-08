package com.jaydenxiao.common.baserx;

import java.io.IOException;

/**
 * Created by Chen on 2017/5/8.
 */

public class ResultException extends IOException {

    private String errMsg;
    private int errCode;

    public ResultException(String errMsg, int errCode){
        this.errMsg = errMsg;
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}