package com.huitian.oamanager.ui.main.model;

import com.huitian.oamanager.api.Api;
import com.huitian.oamanager.bean.MeizhiData;
import com.huitian.oamanager.ui.main.contract.HomeContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Chen on 2017/4/17.
 */

public class HomeModel implements HomeContract.Model {

    @Override
    public Observable<MeizhiData> getPhotosListData(int size, int page) {
        return
                Api.getDefault().getPhotoList(Api.getCacheControl(), size, page)
//                .compose(RxResultHelper.<MeizhiData, BaseRespose<MeizhiData>>handleResult())
                        .compose(RxSchedulers.<MeizhiData>io_main());

    }
}
