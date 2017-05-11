package com.huitian.oamanager.ui.main.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.huitian.oamanager.R;
import com.huitian.oamanager.api.Api;
import com.huitian.oamanager.app.App;
import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.HuiTianResponse;
import com.huitian.oamanager.bean.LoginBean;
import com.huitian.oamanager.bean.LoginMessageEvent;
import com.huitian.oamanager.bean.PaymentZhaiQuanBean;
import com.huitian.oamanager.bean.SalttimeBean;
import com.huitian.oamanager.bean.YMDSales;
import com.huitian.oamanager.bean.ZQCountBean;
import com.huitian.oamanager.ui.user.activity.LoginActivity;
import com.huitian.oamanager.ui.user.activity.ModifyPasswordActivity;
import com.huitian.oamanager.ui.webview.WebViewActivity;
import com.huitian.oamanager.util.MD5Utils;
import com.jaeger.library.StatusBarUtil;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.SPUtils;

import org.apache.commons.codec.binary.Base64;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


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
    @Bind(R.id.main_banner)
    BGABanner mainBanner;
    @Bind(R.id.navigationView)
    NavigationView navigationView;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.delivery_tv)
    TextView deliveryTv;
    @Bind(R.id.shengpi_tv)
    TextView shengpiTv;
    @Bind(R.id.zaiquan_tv)
    TextView zaiquanTv;
    @Bind(R.id.xiadan_tv)
    TextView xiadanTv;
    @Bind(R.id.fahuo_search_tv)
    TextView fahuoSearchTv;
    @Bind(R.id.kucun_search_tv)
    TextView kucunSearchTv;
    @Bind(R.id.data_search_tv)
    TextView dataSearchTv;
    @Bind(R.id.wuliu_search_tv)
    TextView wuliuSearchTv;
    @Bind(R.id.zhaiquan_search_tv)
    TextView zhaiquanSearchTv;
    @Bind(R.id.working_search_tv)
    TextView workingSearchTv;
    @Bind(R.id.current_day_tv)
    TextView currentDayTv;
    @Bind(R.id.current_week_tv)
    TextView currentWeekTv;
    @Bind(R.id.tv_today_sale)
    TextView tvTodaySale;
    @Bind(R.id.tv_month_sale)
    TextView tvMonthSale;
    @Bind(R.id.tv_year_sale)
    TextView tvYearSale;
    @Bind(R.id.tv_zhaiquan)
    TextView tvZhaiquan;
    @Bind(R.id.day_return_money)
    TextView dayReturnMoney;
    @Bind(R.id.tv_month_return_money)
    TextView tvMonthReturnMoney;
    @Bind(R.id.tv_year_return_money)
    TextView tvYearReturnMoney;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.tv_zhanquan_count)
    TextView tvZhanquanCount;
    private ActionBarDrawerToggle mDrawerToggle;
    private long taskTime;
    // 请求握手失败的次数
    private int requestSalttimeFailCount = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getSalttime();
        }
    };
    private boolean isStartLoginActivity;
    private NormalDialog dialog;
    // 是否是第一次进入并且去请求销售额等数据
    private boolean isFirstInitData = true;
    private boolean isCurrentSalttime = false;
    private boolean isCurrentLogin;
    private int num = 0; // 正在加载数据的任务个数

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        setToolBar(toolBar, "");
        // 初始化侧边栏
        initDrawLayout();
        // 沉浸式状态栏
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_D7D), 1);
//        StatusBarUtil.setColorForDrawerLayout(this, drawerLayout,getResources().getColor(R.color.color_D7D), 1);
        // 设置toolbar的标题
        centerIv.setVisibility(View.VISIBLE);
        centerIv.setImageResource(R.mipmap.icon_logo);
        // 初始化轮播图
        initBanner();
        // 初始化日期
        initDate();
        long l = System.currentTimeMillis();
        taskTime = (Constans.expire - l) - (1000 * 60);
        // 判断保存的时间戳时间是否大于当前时间
        if (taskTime > 0) { // 没有过期，在taskTime时间之后自动握手
            isFirstInitData = false;
            initData(); // 请求销售额等数据
            // 在过期前提前一分钟进行握手
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendMessageDelayed(Message.obtain(), taskTime);
        } else { // 在一分钟之内就会过期，立即重新进行握手
            // 一次握手
            getSalttime();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (TextUtils.isEmpty(SPUtils.getSharedStringData(this, Constans.keyStr))) { // 用户没有登录，跳转到登录界面
            finish();
            startActivity(LoginActivity.class);
        } else { // 用户已经登录，获取销售额
            // 获取首页销售额
            getYMDSales();
            // 获取债权信息和回款信息
            getPaymentAndZhaiQuan();
            // 设置用户昵称
            TextView tvNickName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_nick_name);
            String userName = SPUtils.getSharedStringData(mContext, Constans.USER_NICK_NAME);
            if (!TextUtils.isEmpty(userName)) { // 如果昵称不为空，设置
                tvNickName.setText(userName + "，你好!");
            }
            // 获取风险债权提醒个数
            getZQCount(userName);
        }
    }

    /**
     * 风险债权提醒个数
     *
     * @param userName
     */
    private void getZQCount(String userName) {
        mRxManager.add(Api.getDefault().getZQCount(Api.getCacheControl(), Constans.m, Constans.n, Constans.t, Constans.k, userName)
                .compose(RxSchedulers.<HuiTianResponse<ZQCountBean>>io_main()).subscribe(new RxSubscriber<HuiTianResponse<ZQCountBean>>(mContext, false) {

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (num == 0) {
                            startProgressDialog("正在加载数据");
                        }
                        num++;
                    }

                    @Override
                    protected void _onNext(HuiTianResponse<ZQCountBean> response) {
                        if (response.getState() == 1) {
                            if (response.getData().getNum() > 0) { // 提醒个数大于0
                                tvZhanquanCount.setVisibility(View.VISIBLE);
                                tvZhanquanCount.setText(String.valueOf(response.getData().getNum()));
                            } else {
                                tvZhanquanCount.setVisibility(View.GONE);
                            }
                        } else { // 请求数据失败，标记为没有获取到数据
                            isFirstInitData = true;
                        }
                        num--;
                        if (num == 0) {
                            stopProgressDialog();
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        isFirstInitData = true;
                        num--;
                        if (num == 0) {
                            stopProgressDialog();
                        }
                    }
                }));
    }

    private void getPaymentAndZhaiQuan() {
        mRxManager.add(Api.getDefault().getPaymentAndZhaiQuan(Api.getCacheControl(), Constans.m, Constans.n, Constans.t, Constans.k)
                .compose(RxSchedulers.<HuiTianResponse<PaymentZhaiQuanBean>>io_main()).subscribe(new RxSubscriber<HuiTianResponse<PaymentZhaiQuanBean>>(mContext, false) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if (num == 0) {
                            startProgressDialog("正在加载数据");
                        }
                        num++;
                    }

                    @Override
                    protected void _onNext(HuiTianResponse<PaymentZhaiQuanBean> response) {
                        if (response.getState() == 1) {
                            tvZhaiquan.setText(String.valueOf(response.getData().getCREDIT_AMOUNT()));
                            dayReturnMoney.setText(String.valueOf(response.getData().getDay()));
                            tvMonthReturnMoney.setText(String.valueOf(response.getData().getMonth()));
                            tvYearReturnMoney.setText(String.valueOf(response.getData().getYear()));
                        } else { // 请求数据失败
                            isFirstInitData = true;
                        }
                        num--;
                        if (num == 0) {
                            stopProgressDialog();
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        isFirstInitData = true;
                        num--;
                        if (num == 0) {
                            stopProgressDialog();
                        }
                    }
                }));
    }

    /**
     * 销售额数据
     */
    private void getYMDSales() {
        mRxManager.add(Api.getDefault().getYMDSales(Api.getCacheControl(), Constans.m, Constans.n, Constans.t, Constans.k)
                .compose(RxSchedulers.<HuiTianResponse<YMDSales>>io_main()).subscribe(new RxSubscriber<HuiTianResponse<YMDSales>>(mContext, false) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if (num == 0) {
                            startProgressDialog("正在加载数据");
                        }
                        num++;
                    }

                    @Override
                    protected void _onNext(HuiTianResponse<YMDSales> response) {
                        if (response.getState() == 1) {
                            tvTodaySale.setText(MD5Utils.formatTosepara(Long.valueOf(response.getData().getDay())));
                            tvMonthSale.setText(String.valueOf(MD5Utils.formatTosepara(Long.valueOf(response.getData().getMonth()))));
                            tvYearSale.setText(String.valueOf(MD5Utils.formatTosepara(Long.valueOf(response.getData().getYear()))));
                        } else {
                            isFirstInitData = true;
                        }
                        num--;
                        if (num == 0) {
                            stopProgressDialog();
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        isFirstInitData = true;
                        num--;
                        if (num == 0) {
                            stopProgressDialog();
                        }
                    }
                }));
    }

    /**
     * 一次握手
     */
    private void getSalttime() {
        if (!isCurrentSalttime) { // 如果当前在握手，那么不去握手
            isCurrentSalttime = true;
            // 获取到10位的随机字符串
            String randomString = MD5Utils.getRandomString(10).toLowerCase();
            String s = null; // 一次握手需要的参数
            try {
                s = MD5Utils.getMD5(Constans.api_key + "_" + MD5Utils.getMD5(Constans.appname + "_" + Constans.ver + "_" + randomString)).toLowerCase();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            Constans.s = s;
            Constans.r = randomString;
            // 进行一次握手
            mRxManager.add(Api.getDefault().getSalttime(Api.getCacheControl(), Constans.s, Constans.r)
                    .compose(RxSchedulers.<HuiTianResponse<SalttimeBean>>io_main()).subscribe(new RxSubscriber<HuiTianResponse<SalttimeBean>>(mContext, false) {
                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        protected void _onNext(HuiTianResponse<SalttimeBean> response) {
                            if (response.getState() == 1) { // 握手成功
                                isCurrentSalttime = false;
                                // 客户端注意处理好提前进行一次认证接口自动调用和异常自动补偿认证逻辑以防接口认证失败
                                // PHP的时间戳需要乘以1000才跟java获取的相同
                                long expire = (long) response.getData().getExpire() * 1000L;
                                long l = System.currentTimeMillis();
                                // 服务器返回的时间戳 - 当前时间戳 = 时间戳有效期（在有效期失效之前需要提前进行一次握手） 提前一分钟去握手
                                taskTime = (expire - l) - (1000 * 60);
                                if (taskTime < 0) { // 如果taskTime为负数，设置一个默认值 1分钟
                                    taskTime = 1 * 6000;
                                }
                                // 移除handler里面的消息
                                mHandler.removeCallbacksAndMessages(null);
                                // 开启定时任务
                                mHandler.sendMessageDelayed(Message.obtain(), taskTime);
                                Constans.m = response.getData().getZ();
                                Constans.n = new String(Base64.decodeBase64(response.getData().getY().getBytes()));
                                Constans.t = new String(Base64.decodeBase64(response.getData().getX().getBytes()));
                                Constans.expire = expire;
                                try {
                                    Constans.k = MD5Utils.getMD5(Constans.api_key + "_" + MD5Utils.getMD5(Constans.t + "_" + Constans.n));
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                // 将请求参数信息存入到本地
                                SPUtils.setSharedStringData(mContext, Constans.M, Constans.m);
                                SPUtils.setSharedStringData(mContext, Constans.N, Constans.n);
                                SPUtils.setSharedStringData(mContext, Constans.T, Constans.t);
                                SPUtils.setSharedStringData(mContext, Constans.K, Constans.k);
                                // 时间戳保存到本地
                                SPUtils.setSharedLongData(mContext, Constans.EXPIRE_TIME, expire);
                                if (isStartLoginActivity) { // 是否跳转到登录
                                    finish();
                                    // 跳转登陆界面
                                    startActivity(LoginActivity.class);
                                }

                                if (isFirstInitData) { // 如果是第一次进入MainACtivity，初始化销售额等数据
                                    // 并将boolean值置为false
                                    isFirstInitData = false;
                                    initData();
                                }
                            } else { // 当服务器返回的不是成功 1 ，重新进行一次握手
                                // 请求失败，记录一次失败
                                requestSalttimeFailCount++;
                                if (requestSalttimeFailCount > 5) { // 如果请求握手失败次数大于10，一分钟后在去请求
                                    requestSalttimeFailCount = 0; // 重置失败次数
                                    // 移除handler里面的消息
                                    mHandler.removeCallbacksAndMessages(null);
                                    mHandler.sendMessageDelayed(Message.obtain(), 60 * 1000);
                                } else { // 失败次数小于5，握手
                                    getSalttime();
                                }
                            }
                        }

                        @Override
                        protected void _onError(String message) {
                            // 请求失败，记录一次失败
                            requestSalttimeFailCount++;
                            if (requestSalttimeFailCount > 5) { // 如果请求握手失败次数大于10，一分钟后在去请求
                                requestSalttimeFailCount = 0; // 重置失败次数
                                // 移除handler里面的消息
                                mHandler.removeCallbacksAndMessages(null);
                                mHandler.sendMessageDelayed(Message.obtain(), 60 * 1000);
                            } else { // 失败次数小于5，握手
                                getSalttime();
                            }
                        }
                    }));
        }
    }

    /**
     * 获取当前日期
     */
    private void initDate() {
        final Calendar c = Calendar.getInstance();
        int mMonth = c.get(Calendar.MONTH) + 1;//获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
//        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK)); // 星期几
        String Month = "";
        switch (mMonth) {
            case 1:
                Month = "1月";
                break;
            case 2:
                Month = "2月";
                break;
            case 3:
                Month = "3月";
                break;
            case 4:
                Month = "4月";
                break;
            case 5:
                Month = "5月";
                break;
            case 6:
                Month = "6月";
                break;
            case 7:
                Month = "7月";
                break;
            case 8:
                Month = "8月";
                break;
            case 9:
                Month = "9月";
                break;
            case 10:
                Month = "10月";
                break;
            case 11:
                Month = "11月";
                break;
            case 12:
                Month = "12月";
                break;
        }
        currentDayTv.setText(String.valueOf(mDay));
        currentWeekTv.setText(Month);
    }

    /**
     * 初始化轮播图数据和点击事件
     */
    private void initBanner() {
        mainBanner.setAdapter(new BGABanner.Adapter<ImageView, Integer>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, Integer model, int position) {
                Glide.with(mContext).load(model)
                        .centerCrop()
                        .into(itemView)
                ;
            }
        });
        // 设置数据
        mainBanner.setData(Arrays.asList(R.mipmap.banner_1, R.mipmap.banner_2), null);
    }

    /**
     * 初始化DrawLayout侧边栏
     */
    private void initDrawLayout() {
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open, R.string.close);
        // 实现箭头和三条杠图案切换和抽屉拉合的同步
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        // 让抽屉菜单的图标显示原来的颜色
        navigationView.setItemTextColor(null);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        // 设置侧边栏的文字颜色
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.color_afa);
        navigationView.setItemTextColor(csl);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_modify_pw:
                startActivity(ModifyPasswordActivity.class);
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_logout:
                drawerLayout.closeDrawers();
                showLoginOutDialog();
                break;
        }
        return false;
    }

    /**
     * 退出登陆
     */
    private void showLoginOutDialog() {
        if (dialog == null) {
            dialog = new NormalDialog(mContext);
            dialog.content("是否退出登录")//
                    .title("提示")
                    .titleTextColor(mainColor)
                    .style(NormalDialog.STYLE_TWO)//
                    .titleTextSize(23)//
                    .show();
            dialog.setOnBtnClickL(
                    new OnBtnClickL() {
                        @Override
                        public void onBtnClick() {
                            dialog.dismiss();
                        }
                    },
                    new OnBtnClickL() {
                        @Override
                        public void onBtnClick() { // 退出登陆
                            loginOut();
                            dialog.dismiss();
                        }
                    });
        } else {
            dialog.show();
        }
    }

    @OnClick({R.id.right_iv, R.id.delivery_tv, R.id.shengpi_tv, R.id.zaiquan_tv, R.id.xiadan_tv, R.id.fahuo_search_tv, R.id.kucun_search_tv, R.id.data_search_tv, R.id.wuliu_search_tv, R.id.zhaiquan_search_tv, R.id.working_search_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_iv:
                break;
            case R.id.delivery_tv:
                showShortToast("敬请期待");
                break;
            case R.id.shengpi_tv:
                showShortToast("敬请期待");
//                ToastUitl.showShort("审批担保");
                break;
            case R.id.zaiquan_tv:
                startWebViewActivity(Constans.RISK_LIST_SEACH);
//                showShortToast("敬请期待");
//                ToastUitl.showShort("债权提醒");
                break;
            case R.id.xiadan_tv:
                showShortToast("敬请期待");
//                ToastUitl.showShort("销售下单");
                break;
            case R.id.fahuo_search_tv: // 发货查询
                startWebViewActivity(Constans.DELIVER_SEACH);
                break;
            case R.id.kucun_search_tv: // 库存查询
                startWebViewActivity(Constans.STOCK_SEACH);
                break;
            case R.id.data_search_tv:
                showShortToast("敬请期待");
//                ToastUitl.showShort("数据查询");
                break;
            case R.id.wuliu_search_tv: // 物流查询
                startWebViewActivity(Constans.LOGISTICS_SEACH);
                break;
            case R.id.zhaiquan_search_tv: // 债权查询
                startWebViewActivity(Constans.CREDITOR_SEACH);
                break;
            case R.id.working_search_tv: // 汇总查询
                startWebViewActivity(Constans.AGGREGATE_SEACH);
                break;
        }
    }

    /**
     * 打开webviewActivity
     */
    private void startWebViewActivity(int type) {
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(Constans.WEBVIEW_TYPE, type);
        startActivity(intent);
    }

    /**
     * 退出登陆接口
     */
    private void loginOut() {
        mRxManager.add(Api.getDefault().loginOut(Api.getCacheControl(), Constans.m, Constans.n, Constans.t, Constans.k)
                .compose(RxSchedulers.<HuiTianResponse<String>>io_main()).subscribe(new RxSubscriber<HuiTianResponse<String>>(mContext, false) {
                    @Override
                    public void onStart() {
                        startProgressDialog("正在退出登陆");
                        super.onStart();
                    }

                    @Override
                    protected void _onNext(HuiTianResponse<String> response) {
                        if (response.getState() == 1) {
                            // 退出登陆，清空keyStr，和cookie,时间戳
                            SPUtils.setSharedStringData(mContext, Constans.keyStr, "");
                            SPUtils.setSharedLongData(App.getAppContext(), Constans.EXPIRE_TIME, 0L);
                            SharedPreferences sp = mContext.getSharedPreferences(Constans.COOKIE_PREF, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear().commit();
                            showShortToast("退出登陆成功");
                            // 标记需要打开登录Activity
                            isStartLoginActivity = true;
                            // 重新握手
                            getSalttime();
                        } else {
                            showShortToast(response.getMessage());
                        }
                        stopProgressDialog();
                    }

                    @Override
                    protected void _onError(String message) {
                        isFirstInitData = true;
                        stopProgressDialog();
                    }
                }));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginMessageEvent event) {
        switch (event.getType()) {
            case Constans.LOGIN_MESSAGE: // 登录失效,重新登录
                login();
                break;
            case Constans.SALTTIME_MESSAGE: // 握手失效，重新握手
                getSalttime();
                break;
        }
    }

    /**
     * 自动登录
     */
    private void login() {
        if (!isCurrentLogin) {
            isCurrentLogin = true;
            String keyStr = SPUtils.getSharedStringData(mContext, Constans.keyStr);
            String registrationId = SPUtils.getSharedStringData(mContext, Constans.REGISTRATIONID);
            mRxManager.add(Api.getDefault().getLoginUser(Api.getCacheControl(), Constans.m, Constans.n, Constans.t, "", "", Constans.k, keyStr, registrationId)
                    .compose(RxSchedulers.<HuiTianResponse<LoginBean>>io_main()).subscribe(new RxSubscriber<HuiTianResponse<LoginBean>>(mContext, false) {
                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        protected void _onNext(HuiTianResponse<LoginBean> response) {
                            if (response.getState() == 1) { // 登陆成功
                                isCurrentLogin = false;
                                // 保存用户昵称
                                SPUtils.setSharedStringData(mContext, Constans.USER_NICK_NAME, response.getData().getUser_info().getUSER_NAME());
                                // 保存keystr信息，用于登陆失效免密登陆
                                SPUtils.setSharedStringData(mContext, Constans.keyStr, response.getData().getKey_str());
                                stopProgressDialog();
                                // 判断销售额数据是否调取过，如果没有调用，调用
                                if (isFirstInitData) { // 如果是第一次进入MainACtivity，初始化销售额等数据
                                    // 并将boolean值置为false
                                    isFirstInitData = false;
                                    initData();
                                }
                            } else { // 自动登录失败
                                startActivity(LoginActivity.class);
                                finish();
                            }
                        }

                        @Override
                        protected void _onError(String message) {
                            isCurrentLogin = false;
                            stopProgressDialog();
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }));
        }
    }

    private static Boolean isExit = false;

    @Override
    public void onBackPressed() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            showShortToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainBanner != null)
            mainBanner.startAutoPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mainBanner != null)
            mainBanner.stopAutoPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除所有的消息
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

}
