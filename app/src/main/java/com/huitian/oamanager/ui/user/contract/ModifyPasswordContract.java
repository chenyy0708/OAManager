package com.huitian.oamanager.ui.user.contract;

import com.huitian.oamanager.bean.HuiTianResponse;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import rx.Observable;

/**
 * 2017-05-02 上午11:27
 *
 * @author koumanwei
 * @version 1.0
 */

public interface ModifyPasswordContract {
    interface Model extends BaseModel {
        Observable<HuiTianResponse<String>> changePassword(String oldPassword, String newPassword);
    }

    interface View extends BaseView {
        void changePasswordSuccess(String msg);

        void changePasswordFail(String msg);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void changePassword(String oldPassword, String newPassword);
    }
}
