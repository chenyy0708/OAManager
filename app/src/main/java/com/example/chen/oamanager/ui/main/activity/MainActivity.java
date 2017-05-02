package com.example.chen.oamanager.ui.main.activity;


import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.oamanager.R;
import com.example.chen.oamanager.api.Api;
import com.example.chen.oamanager.app.Constans;
import com.example.chen.oamanager.bean.HuiTianResponse;
import com.example.chen.oamanager.bean.SalttimeBean;
import com.example.chen.oamanager.ui.user.activity.LoginActivity;
import com.example.chen.oamanager.utils.ImageUtils;
import com.example.chen.oamanager.utils.MD5Utils;
import com.jaeger.library.StatusBarUtil;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.SPUtils;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.imagePager.BigImagePagerActivity;

import org.apache.commons.codec.binary.Base64;

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
    private ActionBarDrawerToggle mDrawerToggle;
    private long taskTime;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 握手
            getSalttime();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        setToolBar(toolBar, "");
        initDrawLayout();
        int color = getResources().getColor(R.color.colorPrimary);
        // 沉浸式状态栏
        StatusBarUtil.setColorForDrawerLayout(MainActivity.this, drawerLayout, color, 1);
        // 设置toolbar的标题
        centerIv.setVisibility(View.VISIBLE);
        centerIv.setImageResource(R.mipmap.icon_logo);
        rightIv.setVisibility(View.VISIBLE);
        // 加载圆形图片
        ImageUtils.loadCircle(this, "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1942495469,4050776630&fm=23&gp=0.jpg", rightIv);
        // 初始化轮播图
        initBanner();
        // 初始化日期
        initDate();
        // 判断时间戳时间是否大于当前时间
        taskTime = (Constans.expire - System.currentTimeMillis()) - (1000 * 60);
        if (taskTime > 0) { // 没有过期，在taskTime时间之后自动握手
            // 在过期前提前一分钟进行握手
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendMessageDelayed(Message.obtain(), 6 * 1000);
        } else { // 在一分钟之内就会过期，立即重新进行握手
//            // 一次握手
            getSalttime();
        }
        tvTodaySale.setText(MD5Utils.formatTosepara(25768L));
        tvMonthSale.setText(MD5Utils.formatTosepara(34543545L));
        tvYearSale.setText(MD5Utils.formatTosepara(5743434323L));
    }

    /**
     * 一次握手
     */
    private void getSalttime() {
        // 获取到随机字符串
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
                            // 保存密串有效的最迟时间戳
                            // 当前时间超出最晚时间戳则接口二次认证密匙自动失效,
                            // 客户端需记录保存该失效时间，
                            // 失效时间到期前无需再次调用一次认证接口直至失效时间邻近
                            // 或者失效时间超过再次调用一次认证接口重新进行计算接口密串
                            // 客户端注意处理好提前进行一次认证接口自动调用和异常自动补偿认证逻辑以防接口认证失败
                            long expire = (long) response.getData().getExpire() * 1000L;
                            long l = System.currentTimeMillis();
                            // 服务器返回的时间戳 - 当前时间戳 = 时间戳有效期（在有效期失效之前需要提前进行一次握手） 提前一分钟去握手
                            taskTime = (expire - l) - (1000 * 60);
                            // 移除handler里面的消息
                            mHandler.removeCallbacksAndMessages(null);
                            // 开启定时任务
                            mHandler.sendMessageDelayed(Message.obtain(), taskTime);
                            Constans.m = response.getData().getZ();
                            Constans.n = new String(Base64.decodeBase64(response.getData().getY().getBytes()));
                            Constans.t = new String(Base64.decodeBase64(response.getData().getX().getBytes()));
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
                            showShortToast("一次握手成功");
                        } else { // 当服务器返回的不是成功 1 ，重新进行一次握手
                            getSalttime();
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        // 一次握手失败，重新进行握手
                        getSalttime();
                    }
                }));
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

    private void initBanner() {
        mainBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                ImageUtils.load(mContext, model, itemView);
            }
        });
        // 设置数据
        mainBanner.setData(Arrays.asList("http://img07.tooopen.com/images/20170412/tooopen_sy_205630266491.jpg", "http://img07.tooopen.com/images/20170412/tooopen_sy_205630266491.jpg",
                "http://img07.tooopen.com/images/20170412/tooopen_sy_205630266491.jpg", "http://img07.tooopen.com/images/20170412/tooopen_sy_205630266491.jpg"), null);
        mainBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                BigImagePagerActivity.startImagePagerActivity(MainActivity.this, Arrays.asList(model), 0);
            }
        });
    }

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
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }


    @OnClick({R.id.right_iv, R.id.delivery_tv, R.id.shengpi_tv, R.id.zaiquan_tv, R.id.xiadan_tv, R.id.fahuo_search_tv, R.id.kucun_search_tv, R.id.data_search_tv, R.id.wuliu_search_tv, R.id.zhaiquan_search_tv, R.id.working_search_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_iv:
                startActivity(LoginActivity.class);
                break;
            case R.id.delivery_tv:
                ToastUitl.showShort("发货担保");
                // 退出登陆
                loginOut();
                break;
            case R.id.shengpi_tv:
                ToastUitl.showShort("审批担保");
                break;
            case R.id.zaiquan_tv:
                ToastUitl.showShort("债权提醒");
                break;
            case R.id.xiadan_tv:
                ToastUitl.showShort("销售下单");
                break;
            case R.id.fahuo_search_tv:
                ToastUitl.showShort("发货查询");
                break;
            case R.id.kucun_search_tv:
                ToastUitl.showShort("库存查询");
                break;
            case R.id.data_search_tv:
                ToastUitl.showShort("数据查询");
                break;
            case R.id.wuliu_search_tv:
                ToastUitl.showShort("物理查询");
                break;
            case R.id.zhaiquan_search_tv:
                ToastUitl.showShort("债权查询");
                break;
            case R.id.working_search_tv:
                ToastUitl.showShort("业绩查询");
                break;
        }
    }

    private void loginOut() {
        // 进行一次握手
        mRxManager.add(Api.getDefault().loginOut(Api.getCacheControl(), Constans.m, Constans.n, Constans.t, Constans.k)
                .compose(RxSchedulers.<HuiTianResponse<String>>io_main()).subscribe(new RxSubscriber<HuiTianResponse<String>>(mContext, false) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    protected void _onNext(HuiTianResponse<String> response) {
                        String k = Constans.k;
                        if (response.getState() == 1) {
                            showShortToast(response.getMessage());
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                    }
                }));
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
    }

}
