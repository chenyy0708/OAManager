package com.huitian.oamanager.ui.webview;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.api.ApiConstants;
import com.huitian.oamanager.app.BaseWebViewActivity;
import com.huitian.oamanager.app.Constans;

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
        WebSettings settings = webview.getSettings();
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕大小
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过Js打开新窗口
        webview.setWebChromeClient(new MyWebChromeClient());
        webview.loadUrl(ApiConstants.SERVICE_URL + "/oa/shippingQuery.html");
    }

    @OnClick({R.id.right_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_tv:
                //  筛选
                webview.loadUrl("javascript:showSearch()");
                break;
        }
    }

}
