package com.example.chen.oamanager.ui.usre.presenter;

import com.example.chen.oamanager.bean.HuiTianResponse;
import com.example.chen.oamanager.bean.LoginBean;
import com.example.chen.oamanager.ui.usre.contract.LoginContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

/**
 * Created by Chen on 2017/4/27.
 */

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void loginUser(String userName, String passWord) {
        mRxManage.add(mModel.getLogin(userName, passWord).subscribe(new RxSubscriber<HuiTianResponse>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading("正在登陆");
            }

            @Override
            protected void _onNext(HuiTianResponse loginBeanHuiTianResponse) {
                if (loginBeanHuiTianResponse.getState() == 200) { // 登陆成功
                    // 调用View层登陆成功方法
                    mView.loginSuccess(loginBeanHuiTianResponse);
                    mView.stopLoading();
                } else {
                    mView.loginFail(loginBeanHuiTianResponse.getMessage());
                    mView.stopLoading();
                }
            }

            @Override
            protected void _onError(String message) {
                // 错误
                mView.showErrorTip(message);
                mView.stopLoading();
            }
        }));
    }
}
