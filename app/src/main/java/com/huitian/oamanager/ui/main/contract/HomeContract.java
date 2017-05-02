package com.huitian.oamanager.ui.main.contract;

import com.huitian.oamanager.bean.Meizhi;
import com.huitian.oamanager.bean.MeizhiData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by Chen on 2017/4/17.
 */

public interface HomeContract {

    interface Model extends BaseModel {
        //请求获取图片
        Observable <MeizhiData> getPhotosListData(int size, int page);
    }

    interface View extends BaseView {
        //返回获取的图片
        void returnPhotosListData(List<Meizhi> photoGirls);
    }
    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取图片请求
        public abstract void getPhotosListDataRequest(int size, int page);
    }
}
