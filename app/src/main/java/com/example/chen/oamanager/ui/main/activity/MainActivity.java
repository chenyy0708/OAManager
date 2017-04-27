package com.example.chen.oamanager.ui.main.activity;

import android.icu.util.Calendar;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.oamanager.R;
import com.example.chen.oamanager.api.Api;
import com.example.chen.oamanager.app.Constans;
import com.example.chen.oamanager.bean.HuiTianResponse;
import com.example.chen.oamanager.bean.SalttimeBean;
import com.example.chen.oamanager.ui.usre.activity.LoginActivity;
import com.example.chen.oamanager.utils.ImageUtils;
import com.example.chen.oamanager.utils.MD5Utils;
import com.jaeger.library.StatusBarUtil;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.imagePager.BigImagePagerActivity;

import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
    private ActionBarDrawerToggle mDrawerToggle;

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
        initData();
        // 一次握手
        getSalttime();
    }

    /**
     * 一次握手
     */
    private void getSalttime() {
        // 获取到随机字符串
//        String randomString = MD5Utils.getRandomString(10).toLowerCase();
        String randomString = "sdhurerf!@";
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
                            int expire = response.getData().getExpire();
                            Constans.m = response.getData().getZ();
                            Constans.n = new String(Base64.decodeBase64(response.getData().getY().getBytes()));
                            Constans.t = new String(Base64.decodeBase64(response.getData().getX().getBytes()));
                            try {
                                Constans.k = MD5Utils.getMD5(Constans.api_key + "_" + MD5Utils.getMD5(Constans.t + "_" + Constans.n));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            Log.d("sfwef", "_onNext: " + Constans.m + "--" + Constans.t + "--" + Constans.k + "--" + Constans.n);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                    }
                }));
    }


    private void initData() {
        final Calendar c = Calendar.getInstance();


        int mMonth = c.get(Calendar.MONTH) + 1;//获取当前月份

        int mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码

        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "Sunday";
        } else if ("2".equals(mWay)) {
            mWay = "Monday";
        } else if ("3".equals(mWay)) {
            mWay = "Tuesday";
        } else if ("4".equals(mWay)) {
            mWay = "Wednesday";
        } else if ("5".equals(mWay)) {
            mWay = "Thursday";
        } else if ("6".equals(mWay)) {
            mWay = "Friday";
        } else if ("7".equals(mWay)) {
            mWay = "Saturday";
        }

        currentDayTv.setText(String.valueOf(mDay));
        currentWeekTv.setText(mWay);
    }

    private void initBanner() {
        mainBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                ImageUtils.load(mContext, model, itemView);
            }
        });
        // 设置数据
        mainBanner.setData(Arrays.asList("http://pic.58pic.com/58pic/13/70/90/29358PICQjG_1024.jpg", "http://img5.imgtn.bdimg.com/it/u=2573369149,354273250&fm=23&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=1671693453,3143654611&fm=23&gp=0.jpg", "http://img3.imgtn.bdimg.com/it/u=4092353190,3027758101&fm=23&gp=0.jpg"), null);
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
}
