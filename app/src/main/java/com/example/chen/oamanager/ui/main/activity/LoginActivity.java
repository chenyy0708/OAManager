package com.example.chen.oamanager.ui.main.activity;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.oamanager.R;
import com.example.chen.oamanager.ui.usre.activity.ForgetPassWordActivity;
import com.jaeger.library.StatusBarUtil;
import com.jaydenxiao.common.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.forget_pw_tv)
    TextView forgetPwTv;
    @Bind(R.id.close_iv)
    ImageView closeIv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        SpannableStringBuilder builder = new SpannableStringBuilder(forgetPwTv.getText().toString());
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan mainColor = new ForegroundColorSpan(getResources().getColor(R.color.mainColor));
        builder.setSpan(mainColor, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgetPwTv.setText(builder);
    }


    @OnClick({R.id.close_iv, R.id.forget_pw_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                finish();
                break;
            case R.id.forget_pw_tv:
                startActivity(ForgetPassWordActivity.class);
                break;
        }
    }
}
