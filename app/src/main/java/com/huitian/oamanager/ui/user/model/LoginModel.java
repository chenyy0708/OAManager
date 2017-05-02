package com.huitian.oamanager.ui.user.model;

import com.huitian.oamanager.api.Api;
import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.HuiTianResponse;
import com.huitian.oamanager.bean.LoginBean;
import com.huitian.oamanager.ui.user.contract.LoginContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Chen on 2017/4/27.
 */

public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<HuiTianResponse<LoginBean>> getLogin(String userName, String passWord,String keyStr) {
        String keyStr1 = keyStr;
        return Api.getDefault().getLoginUser(Api.getCacheControl(), Constans.m, Constans.n, Constans.t, userName, passWord, Constans.k,keyStr)
//                .compose(RxResultHelper.<MeizhiData, BaseRespose<MeizhiData>>handleResult())
                .compose(RxSchedulers.<HuiTianResponse<LoginBean>>io_main());
    }
}
