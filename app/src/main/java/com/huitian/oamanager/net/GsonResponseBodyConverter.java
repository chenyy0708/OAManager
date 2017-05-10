package com.huitian.oamanager.net;

import com.google.gson.Gson;
import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.HuiTianErrorResponse;
import com.huitian.oamanager.bean.HuiTianResponse;
import com.huitian.oamanager.bean.LoginMessageEvent;
import com.jaydenxiao.common.baserx.ResultException;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Chen on 2017/5/8.
 */

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody,
        T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    /**
     * 针对数据返回成功、错误不同类型字段处理
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            HuiTianResponse result = gson.fromJson(response, HuiTianResponse.class);
            int code = result.getState();
            if (code == 1) {
                return gson.fromJson(response, type);
            } else { // 服务器返回失败，抽取失败信息
                HuiTianErrorResponse errResponse = gson.fromJson(response, HuiTianErrorResponse.class);
                if (code == -1006) { // 用户登录失效,需要自动登录
                    EventBus.getDefault().post(new LoginMessageEvent("login", Constans.LOGIN_MESSAGE));
                    throw new ResultException(errResponse.getData(), code);
                } if(code == -1005){ // 认证秘钥失效，重新认证
                    EventBus.getDefault().post(new LoginMessageEvent("login", Constans.LOGIN_MESSAGE));
                    throw new ResultException(errResponse.getData(), code);
                } else {
                    throw new ResultException(errResponse.getData(), code);
                }
            }
        } finally {
            value.close();
        }
    }
}