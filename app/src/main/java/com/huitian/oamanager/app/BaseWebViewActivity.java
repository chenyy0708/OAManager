package com.huitian.oamanager.app;

import android.graphics.Bitmap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huitian.oamanager.ui.webview.JavaScriptObject;
import com.jaydenxiao.common.base.BaseActivity;

/**
 * Created by Chen on 2017/5/2.
 */

public abstract class BaseWebViewActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return getWebViewLayoutId();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initWebView();
    }


    //获取布局文件
    public abstract int getWebViewLayoutId();

    //初始化view
    public abstract void initWebView();

    /**
     * 初始化WebView
     */
    public void initWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
//        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        //设置数据库缓存路径
//        webView.getSettings().setDatabasePath(appCachePath);
//        webView.getSettings().setAppCachePath(appCachePath);
//        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setAppCacheEnabled(true);
        // 提供给H5的方法
        webView.addJavascriptInterface(new JavaScriptObject(mContext), "Android");
//        webView.setWebViewClient(new MyWebViewClient());
        webView.clearCache(false);//支持缓存
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }
    }

}
