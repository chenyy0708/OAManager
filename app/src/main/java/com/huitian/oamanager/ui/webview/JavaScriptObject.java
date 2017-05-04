package com.huitian.oamanager.ui.webview;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.huitian.oamanager.ui.user.activity.LoginActivity;
import com.jaydenxiao.common.baseapp.AppManager;


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
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
        // 跳转到登录Activity
        StockWebViewActivity stockWebViewActivity = (StockWebViewActivity) mContext;
        // 关闭打开的WebViewActivity
        AppManager.getAppManager().finishActivity(StockWebViewActivity.class);
    }

}
