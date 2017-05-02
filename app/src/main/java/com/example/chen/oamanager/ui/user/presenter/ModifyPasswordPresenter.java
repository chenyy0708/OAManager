package com.example.chen.oamanager.ui.user.presenter;

import com.example.chen.oamanager.bean.HuiTianResponse;
import com.example.chen.oamanager.ui.user.contract.ModifyPasswordContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

/**
 * 2017-05-02 上午11:25
 *
 * @author koumanwei
 * @version 1.0
 */

public class ModifyPasswordPresenter extends ModifyPasswordContract.Presenter {
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        mRxManage.add(mModel.changePassword(oldPassword, newPassword).subscribe(new RxSubscriber<HuiTianResponse<String>>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading("修改中...");
            }

            @Override
            protected void _onNext(HuiTianResponse<String> stringHuiTianResponse) {
                if (stringHuiTianResponse.getState() == 1) {
                    mView.changePasswordSuccess(stringHuiTianResponse.getMessage());
                } else {
                    mView.changePasswordFail(stringHuiTianResponse.getMessage());
                }
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
                mView.stopLoading();
            }
        }));
    }
}
