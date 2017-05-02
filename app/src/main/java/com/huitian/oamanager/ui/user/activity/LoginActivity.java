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
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;

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
        SpannableStringBuilder builder = new SpannableStringBuilder(forgetPwTv.getText().toString());
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan mainColor = new ForegroundColorSpan(getResources().getColor(R.color.mainColor));
        builder.setSpan(mainColor, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgetPwTv.setText(builder);
        editTextUsername.setText(SPUtils.getSharedStringData(this, Constans.USERNAME));
        editTextPw.setText(SPUtils.getSharedStringData(this, Constans.PASSWORD));
//        editTextUsername.setText("13871750550");
//        editTextPw.setText("750619");
    }


    @OnClick({R.id.close_iv, R.id.forget_pw_tv, R.id.tv_modify_password, R.id.login_bt, R.id.cb_pw_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                finish();
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
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
                    ToastUitl.showShort("账号或密码不能为空");
                    return;
                }
                mPresenter.loginUser(userName, passWord,"");
//                if(TextUtils.isEmpty(SPUtils.getSharedStringData(mContext, Constans.keyStr))) { // 如果Key_str为空第一次登录，需要调用账号密码登录接口
//                    // 调用Presenter的登陆方法
//                    mPresenter.loginUser(userName, passWord,"");
//                }else{ // key_str不为空，直接调用key_str登录
//                    String sharedStringData = SPUtils.getSharedStringData(mContext, Constans.keyStr);
//                    // 调用Presenter的登陆方法
//                    mPresenter.loginUser("", "",sharedStringData);
//                }
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
            SPUtils.setSharedStringData(this, Constans.USERNAME, editTextUsername.getText().toString().trim());
            SPUtils.setSharedStringData(this, Constans.PASSWORD, editTextPw.getText().toString().trim());
        }
        showShortToast("登陆成功");
        // 保存keystr信息，用于登陆失效免密登陆
        SPUtils.setSharedStringData(mContext, Constans.keyStr, loginBean.getKey_str());
        finish();
    }

    @Override
    public void loginFail(String msg) {
        showShortToast(msg);
    }
}
