package com.huitian.oamanager.ui.webview;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.app.BaseWebViewActivity;
import com.jaydenxiao.common.commonutils.LogUtils;

import butterknife.Bind;

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
        startProgressDialog("正在加载");
        setToolBar(toolBar, "");
        // 设置图标
        toolBar.setBackgroundColor(getResources().getColor(R.color.mainColor));
        // 给左上角的图标加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolBarTitleTv.setText("月汇总数据查询");
        toolBarTitleTv.setTextColor(getResources().getColor(R.color.white));
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initWebView(webview);
        webview.setWebViewClient(new MyWebViewClient());
        webview.loadUrl("https://www.baidu.com/");
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            stopProgressDialog();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            }
        } catch (Exception ex) {
        }
    }
}
