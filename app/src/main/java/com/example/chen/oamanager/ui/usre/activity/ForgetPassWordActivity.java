package com.example.chen.oamanager.ui.usre.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.oamanager.R;
import com.example.chen.oamanager.widget.MyCountTimer;
import com.jaydenxiao.common.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Chen on 2017/4/26.
 */

public class ForgetPassWordActivity extends BaseActivity {
    @Bind(R.id.close_iv)
    ImageView closeIv;
    @Bind(R.id.get_yzm_tv)
    TextView getYzmTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.close_iv, R.id.get_yzm_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                finish();
                break;
            case R.id.get_yzm_tv:
                // 开始计时
                MyCountTimer myCountTimer = new MyCountTimer(getYzmTv,getResources().getColor(R.color.color_fd0),getResources().getColor(R.color.text_color_C7C));
                myCountTimer.start();
                break;
        }
    }
}
