package com.huitian.oamanager.ui.user.contract;

import com.huitian.oamanager.bean.HuiTianResponse;
import com.huitian.oamanager.bean.LoginBean;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import rx.Observable;

/**
 * Created by Chen on 2017/4/17.
 */

public interface LoginContract {

    interface Model extends BaseModel {
        //请求获取图片
        Observable<HuiTianResponse<LoginBean>> getLogin(String userName, String passWord,String keyStr);
    }

    interface View extends BaseView {
        //返回获取的图片
        void loginSuccess(LoginBean loginBean);

        void loginFail(String msg);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起登陆请求
        public abstract void loginUser(String userName, String passWord,String keyStr);
    }
}
