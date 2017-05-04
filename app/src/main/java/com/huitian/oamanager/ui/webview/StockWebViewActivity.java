package com.huitian.oamanager.ui.webview;

import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.app.BaseWebViewActivity;
import com.jaydenxiao.common.commonutils.LogUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Chen on 2017/5/2.
 */

public class StockWebViewActivity extends BaseWebViewActivity {
    @Bind(R.id.tool_bar_title_tv)
    TextView toolBarTitleTv;
    @Bind(R.id.right_iv)
    ImageView rightIv;
    @Bind(R.id.center_iv)
    ImageView centerIv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.tool_bar)
    Toolbar toolBar;
    @Bind(R.id.webview)
    WebView webview;

    @Override
    public int getWebViewLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initWebView() {
        setToolBar(toolBar, "");
        // 设置图标
        toolBar.setBackgroundColor(getResources().getColor(R.color.mainColor));
        // 给左上角的图标加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolBarTitleTv.setText("发货查询");
        toolBarTitleTv.setTextColor(getResources().getColor(R.color.white));
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText("筛选");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initWebView(webview);
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("http://192.168.1.180:82/oa/shippingQuery.html");
    }

    @OnClick({R.id.right_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_tv:
                showShortToast("筛选");
                break;
        }
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtils.logd(view.getUrl());
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
