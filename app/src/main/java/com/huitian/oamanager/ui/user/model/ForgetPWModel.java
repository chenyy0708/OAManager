package com.huitian.oamanager.ui.user.model;

import com.huitian.oamanager.api.Api;
import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.GenCodeBean;
import com.huitian.oamanager.bean.HuiTianResponse;
import com.huitian.oamanager.ui.user.contract.ForgetPWContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Chen on 2017/4/27.
 */

public class ForgetPWModel implements ForgetPWContract.Model {

    @Override
    public Observable<HuiTianResponse<GenCodeBean>> genCode(String phone) {
        return Api.getDefault().genCode(Api.getCacheControl(), Constans.m, Constans.n, Constans.t,phone, Constans.k)
                .compose(RxSchedulers.<HuiTianResponse<GenCodeBean>>io_main());
    }

    @Override
    public Observable<HuiTianResponse<String>> forgetPassword(String phone, String code, String password, String confirmPassword) {
        return Api.getDefault().forgetPassword(Api.getCacheControl(), Constans.m, Constans.n, Constans.t, Constans.k
        ,phone,code,password,confirmPassword)
                .compose(RxSchedulers.<HuiTianResponse<String>>io_main());
    }
}
