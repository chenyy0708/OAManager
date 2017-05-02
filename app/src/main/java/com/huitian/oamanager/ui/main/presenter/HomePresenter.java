package com.huitian.oamanager.ui.main.presenter;

import com.huitian.oamanager.R;
import com.huitian.oamanager.bean.MeizhiData;
import com.huitian.oamanager.ui.main.contract.HomeContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

/**
 * Created by Chen on 2017/4/17.
 */

public class HomePresenter extends HomeContract.Presenter {

    @Override
    public void getPhotosListDataRequest(int size, int page) {
        mRxManage.add(mModel.getPhotosListData(size, page).subscribe(new RxSubscriber<MeizhiData>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(MeizhiData meizhiData) {
                mView.returnPhotosListData(meizhiData.getResults());
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
