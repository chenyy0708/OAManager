package com.huitian.oamanager.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.ui.user.activity.LoginActivity;
import com.jaydenxiao.common.baseapp.AppManager;
import com.jaydenxiao.common.commonutils.SPUtils;

/**
 * Created by Chen on 2017/5/4.
 */

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                jumpActivity();
            }
        }, 3000);
    }

    private void jumpActivity() {
        if (!TextUtils.isEmpty(SPUtils.getSharedStringData(this, Constans.keyStr))) { // 已登录，进入主界面
            startActivity(MainActivity.class);
        } else {
            startActivity(LoginActivity.class);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().AppExit(this, false);
    }
}
