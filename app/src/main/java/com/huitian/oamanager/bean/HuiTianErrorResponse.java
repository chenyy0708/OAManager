package com.huitian.oamanager.bean;

/**
 * Created by Chen on 2017/4/27.
 */

public class HuiTianErrorResponse<T> {
    private int state;
    private String message;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
