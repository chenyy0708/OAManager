package com.example.chen.oamanager.ui.usre.model;

import com.example.chen.oamanager.api.Api;
import com.example.chen.oamanager.app.Constans;
import com.example.chen.oamanager.bean.HuiTianResponse;
import com.example.chen.oamanager.bean.LoginBean;
import com.example.chen.oamanager.ui.usre.contract.LoginContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Chen on 2017/4/27.
 */

public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<HuiTianResponse<LoginBean>> getLogin(String userName, String passWord) {
        String k = Constans.k;
        return Api.getDefault().getLoginUser(Api.getCacheControl(), Constans.m, Constans.n, Constans.t, userName, passWord, Constans.k)
//                .compose(RxResultHelper.<MeizhiData, BaseRespose<MeizhiData>>handleResult())
                .compose(RxSchedulers.<HuiTianResponse<LoginBean>>io_main());
    }
}
