package com.huitian.oamanager.ui.webview;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huitian.oamanager.R;
import com.huitian.oamanager.app.BaseWebViewActivity;
import com.jaydenxiao.common.commonwidget.OnDoubleClickListener;

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
        setToolBar(toolBar, "");
        // 设置图标
        toolBar.setBackgroundColor(getResources().getColor(R.color.mainColor));
        // 给左上角的图标加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolBarTitleTv.setText("月汇总数据查询");
        toolBarTitleTv.setTextColor(getResources().getColor(R.color.white));
        toolBar.setNavigationOnClickListener(new OnDoubleClickListener() {
            @Override
            protected void onDoubleClick(View v) {
                finish();
            }
        });
        initWebView(webview);
        webview.loadUrl("https://www.baidu.com/");
    }

}
