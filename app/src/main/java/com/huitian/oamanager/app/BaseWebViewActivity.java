package com.huitian.oamanager.app;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huitian.oamanager.ui.webview.JavaScriptObject;
import com.huitian.oamanager.ui.webview.MyWebChromeClient;
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
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 提供给H5的方法
        webView.addJavascriptInterface(new JavaScriptObject(mContext), "Android");
        webView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕大小
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过Js打开新窗口
        webView.clearCache(false);//支持缓存
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.setWebChromeClient(new MyWebChromeClient());
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
            view.loadUrl(url);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }
    }

}
