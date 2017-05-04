package com.huitian.oamanager.ui.main.activity;

import android.os.Handler;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.app.Constans;
import com.jaydenxiao.common.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by Chen on 2017/5/4.
 */

public class SplashActivity extends BaseActivity {

    @Bind(R.id.tv_version)
    TextView tvVersion;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
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

}
