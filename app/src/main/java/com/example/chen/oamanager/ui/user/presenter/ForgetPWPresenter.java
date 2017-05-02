package com.example.chen.oamanager.ui.user.presenter;

import com.example.chen.oamanager.bean.HuiTianResponse;
import com.example.chen.oamanager.ui.user.contract.ForgetPWContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

/**
 * Created by Chen on 2017/4/27.
 */

public class ForgetPWPresenter extends ForgetPWContract.Presenter {

    @Override
    public void genCode(String phone) {
        mRxManage.add(mModel.genCode(phone).subscribe(new RxSubscriber<HuiTianResponse<String>>(mContext, false) {
            @Override
            protected void _onNext(HuiTianResponse<String> response) {
                if (response.getState() == 1) { // 获取验证码成功
                    // 调用View层登陆成功方法
                    mView.genCodeSuccess();
                } else { // 获取验证码失败,返回错误信息
                    mView.genCodeFail(response.getMessage());
                }
            }

            @Override
            protected void _onError(String message) {
                // 错误
                mView.showErrorTip(message);
            }
        }));
    }

    @Override
    public void forgetPassword(String phone, String code, String password, String confirmPassword) {
        mRxManage.add(mModel.forgetPassword(phone,code,password,confirmPassword).subscribe(new RxSubscriber<HuiTianResponse<String>>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading("正在提交");
            }

            @Override
            protected void _onNext(HuiTianResponse<String> response) {
                if (response.getState() == 1) { // 成功
                    // 调用View层登陆成功方法
                    mView.forgetPasswordSuccess();
                } else { // 失败,返回错误信息
                    mView.forgetPasswordFail(response.getMessage());
                }
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.stopLoading();
                // 错误
                mView.showErrorTip(message);
            }
        }));
    }
}
