package com.huitian.oamanager.ui.user.activity;

import android.support.v7.widget.AppCompatCheckBox;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.LoginBean;
import com.huitian.oamanager.ui.user.contract.LoginContract;
import com.huitian.oamanager.ui.user.model.LoginModel;
import com.huitian.oamanager.ui.user.presenter.LoginPresenter;
import com.huitian.oamanager.util.PhoneNumberUtils;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginPresenter, LoginModel> implements LoginContract.View {
    @Bind(R.id.forget_pw_tv)
    TextView forgetPwTv;
    @Bind(R.id.close_iv)
    ImageView closeIv;
    @Bind(R.id.tv_modify_password)
    TextView tvModifyPassword;
    @Bind(R.id.editText_username)
    EditText editTextUsername;
    @Bind(R.id.editText_pw)
    EditText editTextPw;
    @Bind(R.id.login_bt)
    Button loginBt;
    @Bind(R.id.cb_pw_select)
    AppCompatCheckBox cbPwSelect;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        // 忘记密码textview颜色部分变化
        SpannableStringBuilder builder = new SpannableStringBuilder(forgetPwTv.getText().toString());
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan mainColor = new ForegroundColorSpan(getResources().getColor(R.color.mainColor));
        builder.setSpan(mainColor, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgetPwTv.setText(builder);
        // 将保存的账号和密码输入到EditText中
        editTextUsername.setText(SPUtils.getSharedStringData(this, Constans.USERNAME));
        editTextPw.setText(SPUtils.getSharedStringData(this, Constans.PASSWORD));
    }


    @OnClick({R.id.close_iv, R.id.forget_pw_tv, R.id.tv_modify_password, R.id.login_bt, R.id.cb_pw_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                onBackPressed();
                break;
            case R.id.forget_pw_tv:
                startActivity(ForgetPassWordActivity.class);
                break;
            case R.id.tv_modify_password:
                startActivity(ModifyPasswordActivity.class);
                break;
            case R.id.login_bt: // 登陆
                String userName = editTextUsername.getText().toString().trim();
                String passWord = editTextPw.getText().toString().trim();
                if (!PhoneNumberUtils.isMobileNO(userName)) {
                    showShortToast("手机号码格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
                    ToastUitl.showShort("账号或密码不能为空");
                    return;
                }
                mPresenter.loginUser(userName, passWord, "");
                break;
        }
    }

    @Override
    public void showLoading(String title) {
        startProgressDialog(title);
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {
        showShortToast(msg);
    }

    @Override
    public void loginSuccess(LoginBean loginBean) {
        // 记住用户名
        SPUtils.setSharedStringData(this, Constans.USERNAME, editTextUsername.getText().toString().trim());
        if (cbPwSelect.isChecked()) {
            SPUtils.setSharedStringData(this, Constans.PASSWORD, editTextPw.getText().toString().trim());
        }
        // 保存用户昵称
        SPUtils.setSharedStringData(mContext,Constans.USER_NICK_NAME,loginBean.getUser_info().getUSER_NAME());
        // 保存keystr信息，用于登陆失效免密登陆
        SPUtils.setSharedStringData(mContext, Constans.keyStr, loginBean.getKey_str());
        // 设置结果，并进行传送
        this.setResult(Constans.LOGIN_ACTIVITY);
        finish();
    }

    @Override
    public void loginFail(String msg) {
        showShortToast(msg);
    }

    private static Boolean isExit = false;

    @Override
    public void onBackPressed() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            showShortToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            // 设置结果，并进行传送
            this.setResult(Constans.EXIT_SYSTEM);
            finish();
        }
    }
}
