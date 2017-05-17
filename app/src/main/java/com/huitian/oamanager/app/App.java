package com.huitian.oamanager.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.huitian.oamanager.R;
import com.jaydenxiao.common.BuildConfig;
import com.jaydenxiao.common.baseapp.BaseApplication;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.weavey.loading.lib.LoadingLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Chen on 2017/4/17.
 */

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化logger
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
        // 设置极光推送Debug模式
        JPushInterface.setDebugMode(true);
        // 初始化极光推送
        JPushInterface.init(this);
        // 得到保存请求参数信息
        initSPData();
        // 全局捕获异常
//        Thread.setDefaultUncaughtExceptionHandler(new MobileSafeHanlder());
        // 初始化状态布局
        initLodingLayout();
    }

    private void initLodingLayout() {
        LoadingLayout.getConfig()
                .setErrorText("出错啦~请稍后重试！")
                .setEmptyText("抱歉，暂无数据")
                .setNoNetworkText("无网络连接，请检查您的网络···")
                .setErrorImage(R.mipmap.error)
                .setEmptyImage(R.mipmap.empty)
                .setNoNetworkImage(R.mipmap.no_network)
                .setAllTipTextColor(R.color.black)
                .setAllTipTextSize(14)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.black)
                .setReloadButtonWidthAndHeight(150,40);
    }

    private void initSPData() {
        // 将请求参数信息存入到本地
        Constans.m = SPUtils.getSharedStringData(App.getAppContext(), Constans.M);
        Constans.n = SPUtils.getSharedStringData(App.getAppContext(), Constans.N);
        Constans.t = SPUtils.getSharedStringData(App.getAppContext(), Constans.T);
        Constans.k = SPUtils.getSharedStringData(App.getAppContext(), Constans.K);
        // 时间戳保存到本地
        Constans.expire = SPUtils.getSharedLongData(App.getAppContext(), Constans.EXPIRE_TIME);
        // 当前版本号
//        Constans.ver = "v" + getAppVersionName(App.getAppContext());
    }

    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @return App版本号
     */
    public static String getAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    public static String getAppVersionName(Context context, String packageName) {
        if (isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static boolean isSpace(String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 全局捕获异常
     */
    private class MobileSafeHanlder implements Thread.UncaughtExceptionHandler {
        // 当应用发生未捕获异常时, 这个方法会被调用
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            // 2. 记录错误日志
            try {
                PrintStream writer = new PrintStream(new File(Environment
                        .getExternalStorageDirectory().getAbsolutePath(), "error08.log"));
                ex.printStackTrace(writer);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 1. 闪退
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
