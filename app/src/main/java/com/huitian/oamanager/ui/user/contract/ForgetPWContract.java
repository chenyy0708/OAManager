package com.huitian.oamanager.ui.user.contract;

import com.huitian.oamanager.bean.HuiTianResponse;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import rx.Observable;

/**
 * Created by Chen on 2017/4/17.
 */

public interface ForgetPWContract {

    interface Model extends BaseModel {
        // 请求验证码
        Observable<HuiTianResponse<String>> genCode(String phone);
        // 提交
        Observable<HuiTianResponse<String>> forgetPassword(String phone,String code,String password,String confirmPassword);
    }

    interface View extends BaseView {
        void genCodeSuccess();

        void genCodeFail(String msg);

        void forgetPasswordSuccess();

        void forgetPasswordFail(String msg);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        // 发起登陆请求
        public abstract void genCode(String phone);
        // 忘记密码
        public abstract void forgetPassword(String phone,String code,String password,String confirmPassword);
    }
}
