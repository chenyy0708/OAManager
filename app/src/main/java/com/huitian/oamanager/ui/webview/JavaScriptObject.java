package com.huitian.oamanager.ui.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;


/**
 * Created by Chen on 2017/3/30.
 */

public class JavaScriptObject {

    private Context mContext;

    public JavaScriptObject(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 友盟regID
     */
    @JavascriptInterface
    public void nativeShare(String data) {

    }

}
