package com.example.chen.oamanager.ui.usre.contract;

import com.example.chen.oamanager.bean.HuiTianResponse;
import com.example.chen.oamanager.bean.LoginBean;
import com.example.chen.oamanager.bean.Meizhi;
import com.example.chen.oamanager.bean.MeizhiData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by Chen on 2017/4/17.
 */

public interface LoginContract {

    interface Model extends BaseModel {
        //请求获取图片
        Observable<HuiTianResponse<LoginBean>> getLogin(String userName, String passWord);
    }

    interface View extends BaseView {
        //返回获取的图片
        void loginSuccess(LoginBean loginBean);

        void loginFail(String msg);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起登陆请求
        public abstract void loginUser(String userName, String passWord);
    }
}
