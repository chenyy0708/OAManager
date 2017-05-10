package com.huitian.oamanager.ui.webview;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.LoginMessageEvent;
import com.huitian.oamanager.ui.user.activity.LoginActivity;
import com.jaydenxiao.common.baseapp.AppManager;
import com.jaydenxiao.common.commonutils.SPUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Chen on 2017/3/30.
 */

public class JavaScriptObject {

    private Context mContext;

    public JavaScriptObject(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 跳转到LoginActivity
     */
    @JavascriptInterface
    public void jumpLoginActivity() {
        // 关闭所有界面
        AppManager.getAppManager().finishAllActivity();
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }

    /**
     * 跳转到LoginActivity
     */
    @JavascriptInterface
    public String getKeyStr() {
        String keyStr = SPUtils.getSharedStringData(mContext, Constans.keyStr);
        return keyStr;
    }

    /**
     * 改变标题栏的title
     */
    @JavascriptInterface
    public void changeTitle(String title) {
        // 发送消息
        EventBus.getDefault().post(new LoginMessageEvent(title,Constans.CHANGE_TITLE));
    }

}
