package com.huitian.oamanager.ui.webview;

import android.support.v7.widget.Toolbar;
import android.view.View;
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
        webviewType = getIntent().getIntExtra(Constans.WEBVIEW_TYPE, 0);
        initWebView(webview);
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
                finish();
            }
        });
        toolBarTitleTv.setTextColor(getResources().getColor(R.color.white));
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText("筛选");
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
