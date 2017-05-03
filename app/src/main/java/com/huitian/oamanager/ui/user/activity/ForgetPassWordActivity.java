package com.huitian.oamanager.ui.user.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.bean.GenCodeBean;
import com.huitian.oamanager.ui.user.contract.ForgetPWContract;
import com.huitian.oamanager.ui.user.model.ForgetPWModel;
import com.huitian.oamanager.ui.user.presenter.ForgetPWPresenter;
import com.huitian.oamanager.util.PhoneNumberUtils;
import com.huitian.oamanager.widget.CountDownTimerUtils;
import com.jaydenxiao.common.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Chen on 2017/4/26.
 */

public class ForgetPassWordActivity extends BaseActivity<ForgetPWPresenter, ForgetPWModel> implements ForgetPWContract.View {
    @Bind(R.id.close_iv)
    ImageView closeIv;
    @Bind(R.id.get_yzm_tv)
    TextView getYzmTv;
    @Bind(R.id.logo_1)
    ImageView logo1;
    @Bind(R.id.et_phone_number)
    EditText etPhoneNumber;
    @Bind(R.id.tv_phone_unavailable)
    TextView tvPhoneUnavailable;
    @Bind(R.id.et_yzm)
    EditText etYzm;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.et_comfirm_password)
    EditText etComfirmPassword;
    @Bind(R.id.bt_submit)
    Button btSubmit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.close_iv, R.id.get_yzm_tv, R.id.tv_phone_unavailable, R.id.bt_submit})
    public void onViewClicked(View view) {
        String phoneNumber = "";
        switch (view.getId()) {
            case R.id.close_iv:
                finish();
                break;
            case R.id.get_yzm_tv:
                phoneNumber = etPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    showShortToast("手机号不能为空");
                    return;
                }
                boolean mobileNO = PhoneNumberUtils.isMobileNO(phoneNumber); // 正则验证是否是手机号
                if(mobileNO) { // 是手机号
                    // 调用获取验证码的接口
                    mPresenter.genCode(phoneNumber);
                } else {
                    showShortToast("请输入正确的手机号");
                }
                break;
            case R.id.tv_phone_unavailable: // 手机号码不可用
                break;
            case R.id.bt_submit: // 提交
                phoneNumber = etPhoneNumber.getText().toString().trim(); // 手机号
                String yzmNumber = etYzm.getText().toString().trim(); // 验证码
                String newPWNumber = etNewPassword.getText().toString().trim(); // 新密码
                String comfirmPwNumber = etComfirmPassword.getText().toString().trim(); // 确认密码
                if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(yzmNumber)
                        || TextUtils.isEmpty(newPWNumber) || TextUtils.isEmpty(comfirmPwNumber)) {
                    showShortToast("请填写所有信息");
                    return;
                }
                if (!TextUtils.equals(newPWNumber, comfirmPwNumber)) {
                    showShortToast("两次输入的密码不一致");
                    return;
                }
                // 提交到服务器
                mPresenter.forgetPassword(phoneNumber, yzmNumber, newPWNumber, comfirmPwNumber);
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
    public void genCodeSuccess(GenCodeBean genCodeBean) { // 获取验证码成功
        // 开始计时
        CountDownTimerUtils mCountDownTimer = new CountDownTimerUtils(getYzmTv, 60000, 1000);
        mCountDownTimer.start();
        etYzm.setText(genCodeBean.getCode());
    }

    @Override
    public void genCodeFail(String msg) { // 获取验证码失败
        showShortToast(msg);
    }

    @Override
    public void forgetPasswordSuccess() {
        showShortToast("修改成功");
        finish();
    }

    @Override
    public void forgetPasswordFail(String msg) {
        showShortToast(msg);
    }
}
