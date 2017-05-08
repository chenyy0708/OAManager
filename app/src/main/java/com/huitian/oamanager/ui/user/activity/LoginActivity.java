package com.huitian.oamanager.ui.user.activity;

import android.os.Handler;
import android.os.Message;
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

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.huitian.oamanager.R;
import com.huitian.oamanager.api.Api;
import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.HuiTianResponse;
import com.huitian.oamanager.bean.LoginBean;
import com.huitian.oamanager.bean.SalttimeBean;
import com.huitian.oamanager.ui.main.activity.MainActivity;
import com.huitian.oamanager.ui.user.contract.LoginContract;
import com.huitian.oamanager.ui.user.model.LoginModel;
import com.huitian.oamanager.ui.user.presenter.LoginPresenter;
import com.huitian.oamanager.util.MD5Utils;
import com.huitian.oamanager.util.PhoneNumberUtils;
import com.jaeger.library.StatusBarUtil;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;

import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;
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
    @Bind(R.id.tv_help)
    TextView tvHelp;
    private NormalDialog dialog;


    private long taskTime;
    // 请求握手失败的次数
    private int requestSalttimeFailCount = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getSalttime();
        }
    };

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
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_D7D), 1);
        // 忘记密码textview颜色部分变化
        SpannableStringBuilder builder = new SpannableStringBuilder(forgetPwTv.getText().toString());
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan mainColor = new ForegroundColorSpan(getResources().getColor(R.color.mainColor));
        builder.setSpan(mainColor, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgetPwTv.setText(builder);
        // 将保存的账号和密码输入到EditText中
        editTextUsername.setText(SPUtils.getSharedStringData(this, Constans.USERNAME));
        String password = SPUtils.getSharedStringData(this, Constans.PASSWORD);
        if (!"".equals(password)) {
            editTextPw.setText(password);
            cbPwSelect.setChecked(true);
        }
        // 一次握手
        initData();
    }

    private void initData() {
        long l = System.currentTimeMillis();
        taskTime = (Constans.expire - l) - (1000 * 60);
        // 判断保存的时间戳时间是否大于当前时间
        if (taskTime > 0) { // 没有过期，在taskTime时间之后自动握手
            // 在过期前提前一分钟进行握手
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendMessageDelayed(Message.obtain(), taskTime);
        } else { // 在一分钟之内就会过期，立即重新进行握手
            // 一次握手
            getSalttime();
        }
    }


    @OnClick({R.id.close_iv, R.id.forget_pw_tv, R.id.tv_modify_password, R.id.login_bt, R.id.cb_pw_select, R.id.tv_help})
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
            case R.id.tv_help: // 帮助
                showDialog();
                break;
        }
    }

    /**
     * 拨打客服
     */
    private void showDialog() {
        if (dialog == null) {
            dialog = new NormalDialog(mContext);
            dialog.content("是否拨打客服电话" + getResources().getString(R.string.phone_number))//
                    .title("提示")
                    .titleTextColor(mainColor)
                    .style(NormalDialog.STYLE_TWO)//
                    .titleTextSize(23)//
                    .show();
            dialog.setOnBtnClickL(
                    new OnBtnClickL() {
                        @Override
                        public void onBtnClick() {
                            dialog.dismiss();
                        }
                    },
                    new OnBtnClickL() {
                        @Override
                        public void onBtnClick() { //
                            call(getResources().getString(R.string.phone_number));
                            dialog.dismiss();
                        }
                    });
        } else {
            dialog.show();
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
        SPUtils.setSharedStringData(mContext, Constans.USER_NICK_NAME, loginBean.getUser_info().getUSER_NAME());
        // 保存keystr信息，用于登陆失效免密登陆
        SPUtils.setSharedStringData(mContext, Constans.keyStr, loginBean.getKey_str());
        showShortToast("登陆成功");
        startActivity(MainActivity.class);
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
            finish();
        }
    }


    /**
     * 一次握手
     */
    private void getSalttime() {
        // 获取到10位的随机字符串
        String randomString = MD5Utils.getRandomString(10).toLowerCase();
        String s = null; // 一次握手需要的参数
        try {
            s = MD5Utils.getMD5(Constans.api_key + "_" + MD5Utils.getMD5(Constans.appname + "_" + Constans.ver + "_" + randomString)).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Constans.s = s;
        Constans.r = randomString;
        // 进行一次握手
        mRxManager.add(Api.getDefault().getSalttime(Api.getCacheControl(), Constans.s, Constans.r)
                .compose(RxSchedulers.<HuiTianResponse<SalttimeBean>>io_main()).subscribe(new RxSubscriber<HuiTianResponse<SalttimeBean>>(mContext, false) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    protected void _onNext(HuiTianResponse<SalttimeBean> response) {
                        if (response.getState() == 1) { // 握手成功
                            // 客户端注意处理好提前进行一次认证接口自动调用和异常自动补偿认证逻辑以防接口认证失败
                            // PHP的时间戳需要乘以1000才跟java获取的相同
                            long expire = (long) response.getData().getExpire() * 1000L;
                            long l = System.currentTimeMillis();
                            // 服务器返回的时间戳 - 当前时间戳 = 时间戳有效期（在有效期失效之前需要提前进行一次握手） 提前一分钟去握手
                            taskTime = (expire - l) - (1000 * 60);
                            if (taskTime < 0) { // 如果taskTime为负数，设置一个默认值 1分钟
                                taskTime = 1 * 6000;
                            }
                            // 移除handler里面的消息
                            mHandler.removeCallbacksAndMessages(null);
                            // 开启定时任务
                            mHandler.sendMessageDelayed(Message.obtain(), taskTime);
                            Constans.m = response.getData().getZ();
                            Constans.n = new String(Base64.decodeBase64(response.getData().getY().getBytes()));
                            Constans.t = new String(Base64.decodeBase64(response.getData().getX().getBytes()));
                            Constans.expire = expire;
                            try {
                                Constans.k = MD5Utils.getMD5(Constans.api_key + "_" + MD5Utils.getMD5(Constans.t + "_" + Constans.n));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            // 将请求参数信息存入到本地
                            SPUtils.setSharedStringData(mContext, Constans.M, Constans.m);
                            SPUtils.setSharedStringData(mContext, Constans.N, Constans.n);
                            SPUtils.setSharedStringData(mContext, Constans.T, Constans.t);
                            SPUtils.setSharedStringData(mContext, Constans.K, Constans.k);
                            // 时间戳保存到本地
                            SPUtils.setSharedLongData(mContext, Constans.EXPIRE_TIME, expire);
                        } else { // 当服务器返回的不是成功 1 ，重新进行一次握手
                            // 请求失败，记录一次失败
                            requestSalttimeFailCount++;
                            if (requestSalttimeFailCount > 5) { // 如果请求握手失败次数大于10，一分钟后在去请求
                                requestSalttimeFailCount = 0; // 重置失败次数
                                // 移除handler里面的消息
                                mHandler.removeCallbacksAndMessages(null);
                                mHandler.sendMessageDelayed(Message.obtain(), 60 * 1000);
                            } else { // 失败次数小于5，握手
                                getSalttime();
                            }
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        // 请求失败，记录一次失败
                        requestSalttimeFailCount++;
                        if (requestSalttimeFailCount > 5) { // 如果请求握手失败次数大于10，一分钟后在去请求
                            requestSalttimeFailCount = 0; // 重置失败次数
                            // 移除handler里面的消息
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.sendMessageDelayed(Message.obtain(), 60 * 1000);
                        } else { // 失败次数小于5，握手
                            getSalttime();
                        }
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除所有的消息
        mHandler.removeCallbacksAndMessages(null);
    }

}
