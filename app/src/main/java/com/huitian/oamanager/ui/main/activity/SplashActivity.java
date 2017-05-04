package com.huitian.oamanager.ui.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.app.Constans;
import com.jaydenxiao.common.baseapp.AppManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Chen on 2017/5/4.
 */

public class SplashActivity extends Activity {


    @Bind(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        // 设置版本号
        tvVersion.setText(Constans.ver);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                setResult(Constans.SPLASH_ACT);
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().AppExit(this,false);
    }
}
