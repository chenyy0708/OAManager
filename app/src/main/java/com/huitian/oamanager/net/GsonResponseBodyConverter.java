package com.huitian.oamanager.net;

import com.google.gson.Gson;
import com.huitian.oamanager.bean.HuiTianErrorResponse;
import com.huitian.oamanager.bean.HuiTianResponse;
import com.jaydenxiao.common.baserx.ResultException;

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
            // 这里的type实际类型是 HttpResult<PhoneBean>  PhoneBean就是retData要获取的对象。
            HuiTianResponse result = gson.fromJson(response, HuiTianResponse.class);
            int code = result.getState();
            if (code == 1) {
                return gson.fromJson(response, type);
            } else { // 服务器返回失败，抽取失败信息
                HuiTianErrorResponse errResponse = gson.fromJson(response, HuiTianErrorResponse.class);
                if (code != 0) {
                    throw new ResultException(errResponse.getMessage(), code);
                } else {
                    throw new ResultException(errResponse.getData(), code);
                }
            }
        } finally {
            value.close();
        }
    }
}