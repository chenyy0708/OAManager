package com.example.chen.oamanager.app;

import com.jaydenxiao.common.BuildConfig;
import com.jaydenxiao.common.baseapp.BaseApplication;
import com.jaydenxiao.common.commonutils.LogUtils;

/**
 * Created by Chen on 2017/4/17.
 */

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化logger
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
    }
}
