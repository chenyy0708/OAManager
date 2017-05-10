package com.huitian.oamanager.ui.webview;

import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.api.ApiConstants;
import com.huitian.oamanager.app.BaseWebViewActivity;
import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.LoginMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Chen on 2017/5/2.
 */

public class WebViewActivity extends BaseWebViewActivity {
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
    private int webviewType;

    @Override
    public int getWebViewLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initWebView() {
        EventBus.getDefault().register(this);
        webviewType = getIntent().getIntExtra(Constans.WEBVIEW_TYPE, 0);
        initWebView(webview);
        webview.setWebViewClient(new CustomWebViewClient());
        initToolbar();
        // 根据type，设置不同的url和标题
        switch (webviewType) {
            case Constans.DELIVER_SEACH: // 发货查询
                toolBarTitleTv.setText("发货查询");
                webview.loadUrl(ApiConstants.SERVICE_URL + "/oa/page/shippingQuery.html");
                break;
            case Constans.STOCK_SEACH: // 库存查询
                toolBarTitleTv.setText("库存查询");
                webview.loadUrl(ApiConstants.SERVICE_URL + "/oa/page/stock.html");
                break;
            case Constans.LOGISTICS_SEACH: // 物流查询
                toolBarTitleTv.setText("物流查询");
                webview.loadUrl(ApiConstants.SERVICE_URL + "/oa/page/logistics.html");
                break;
            case Constans.AGGREGATE_SEACH: // 汇总查询
                toolBarTitleTv.setText("汇总查询");
                webview.loadUrl(ApiConstants.SERVICE_URL + "/oa/page/aggregate.html");
                break;
            case Constans.CREDITOR_SEACH: // 债权查询
                toolBarTitleTv.setText("债权查询");
                webview.loadUrl(ApiConstants.SERVICE_URL + "/oa/page/creditor.html");
                break;
            case Constans.RISK_SEACH: // 债权提醒
                toolBarTitleTv.setText("债权查询");
                rightTv.setVisibility(View.GONE); // 隐藏标题右边按钮
                // 获取字段
                String name = getIntent().getStringExtra(Constans.EXTRA_EXTRA_NAME);
                String value = getIntent().getStringExtra(Constans.EXTRA_EXTRA_VALUE);
                webview.loadUrl(ApiConstants.SERVICE_URL + "/oa/page/risk.html" + "?" + name + "=" + value);
                break;
            case Constans.RISK_LIST_SEACH: // 债权提醒列表
                webview.loadUrl(ApiConstants.SERVICE_URL + "/oa/page/riskList.html");
                break;
        }

    }

    // 设置toolbar
    private void initToolbar() {
        setToolBar(toolBar, "");
        // 设置图标
        toolBar.setBackgroundColor(getResources().getColor(R.color.mainColor));
        // 给左上角的图标加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolBarTitleTv.setTextColor(getResources().getColor(R.color.white));
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText("筛选");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginMessageEvent event) {
        switch (event.getType()) {
            case Constans.CHANGE_TITLE: // 改变标题栏
                toolBarTitleTv.setText(event.getMessage());
                break;
        }
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

    @Override
    public void onBackPressed() {
        String url = webview.getUrl();
        boolean contains = url.contains("risk.html");
        if (contains) { // 在风险详情页，加载列表页面
            webview.loadUrl(ApiConstants.SERVICE_URL + "/oa/page/riskList.html");
        } else if (url.contains("riskList.html")) {
            finish();
        } else { // 其他默认
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (webview != null) {
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(url.contains("risk.html")) {
                rightTv.setVisibility(View.GONE);
            }else if(url.contains("riskList.html")) {
                rightTv.setVisibility(View.VISIBLE);
            }
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
