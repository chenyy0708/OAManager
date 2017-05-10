package com.huitian.oamanager.bean;

/**
 * Created by Chen on 2017/5/10.
 */

public class LoginMessageEvent {
    /**
     * 消息的内容
     */
    private String message;
    /**
     * 消息的类型
     */
    private int type;

    public LoginMessageEvent(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }
}
