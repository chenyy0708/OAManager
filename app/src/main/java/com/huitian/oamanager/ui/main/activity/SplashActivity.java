package com.huitian.oamanager.ui.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.huitian.oamanager.api.Api;
import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.HuiTianResponse;
import com.huitian.oamanager.bean.SalttimeBean;
import com.huitian.oamanager.ui.user.activity.LoginActivity;
import com.huitian.oamanager.util.MD5Utils;
import com.jaydenxiao.common.baseapp.AppManager;
import com.jaydenxiao.common.baserx.RxManager;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.SPUtils;

import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Chen on 2017/5/4.
 */

public class SplashActivity extends Activity {


    private long taskTime;
    // 请求握手失败的次数
    private int requestSalttimeFailCount = 0;
    private long startTime;
    private long endTime;
    public RxManager mRxManager;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 把actvity放到application栈中管理
        AppManager.getAppManager().addActivity(this);
        mContext = this;
        mRxManager = new RxManager();
        initView();
    }

    public void initView() {
        startTime = System.currentTimeMillis();
        taskTime = (Constans.expire - startTime) - (1000 * 60);
        // 判断保存的时间戳时间是否大于当前时间
        if (taskTime > 0) { // 没有过期，等待3000进入MainActivity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    jumpActivity();
                }
            }, 3000);
        } else { // 在一分钟之内就会过期，立即重新进行握手
            // 一次握手
            getSalttime();
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
                            endTime = System.currentTimeMillis();
                            // 判断当前时间是否超过3000
                            if (endTime - startTime >= 3000) { // 握手请求超过3000，直接进入主界面
                                finish();
                                startActivity(MainActivity.class);
                            } else { // 没有超过3000
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        jumpActivity();
                                    }
                                }, 3000 - (endTime - startTime));
                            }
                        } else { // 当服务器返回的不是成功 1 ，重新进行一次握手
                            // 请求失败，记录一次失败
                            requestSalttimeFailCount++;
                            if (requestSalttimeFailCount > 5) { // 如果请求握手失败次数大于5，一分钟后在去请求
                                jumpActivity();
                            } else { // 失败次数小于5，握手
                                getSalttime();
                            }
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        // 请求失败，记录一次失败
                        requestSalttimeFailCount++;
                        if (requestSalttimeFailCount > 5) { // 如果请求握手失败次数大于5，一分钟后在去请求
                            // 移除handler里面的消息
                            jumpActivity();
                        } else { // 失败次数小于5，握手
                            getSalttime();
                        }
                    }
                }));
    }

    private void jumpActivity() {
        if (!TextUtils.isEmpty(SPUtils.getSharedStringData(mContext, Constans.keyStr))) { // 已登录，进入主界面
            startActivity(MainActivity.class);
        } else {
            startActivity(LoginActivity.class);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRxManager.clear();
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
