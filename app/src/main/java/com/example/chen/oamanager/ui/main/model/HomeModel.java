package com.example.chen.oamanager.ui.main.model;

import com.example.chen.oamanager.api.Api;
import com.example.chen.oamanager.api.HostType;
import com.example.chen.oamanager.bean.MeizhiData;
import com.example.chen.oamanager.ui.main.contract.HomeContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import rx.Observable;

/**
 * Created by Chen on 2017/4/17.
 */

public class HomeModel implements HomeContract.Model {

    @Override
    public Observable<MeizhiData> getPhotosListData(int size, int page) {
        return
                Api.getDefault(HostType.GANK_GIRL_PHOTO).getPhotoList(Api.getCacheControl(), size, page)
//                .compose(RxResultHelper.<MeizhiData, BaseRespose<MeizhiData>>handleResult())
                        .compose(RxSchedulers.<MeizhiData>io_main());

    }
}
