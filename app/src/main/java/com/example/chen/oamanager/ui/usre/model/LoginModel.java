package com.example.chen.oamanager.ui.usre.model;

import com.example.chen.oamanager.api.Api;
import com.example.chen.oamanager.bean.HuiTianResponse;
import com.example.chen.oamanager.bean.LoginBean;
import com.example.chen.oamanager.bean.MeizhiData;
import com.example.chen.oamanager.ui.usre.contract.LoginContract;
import com.jaydenxiao.common.baserx.RxResultHelper;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Chen on 2017/4/27.
 */

public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<HuiTianResponse> getLogin(String userName, String passWord) {
        return Api.getDefault().getLoginUser(Api.getCacheControl(), userName, passWord, "26fd705de28ccc531c82930b4138381e")
//                .compose(RxResultHelper.<MeizhiData, BaseRespose<MeizhiData>>handleResult())
                .compose(RxSchedulers.<HuiTianResponse>io_main());
    }
}
