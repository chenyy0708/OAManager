package com.huitian.oamanager.app;

/**
 * 字符串常量类
 * Created by Chen on 2017/4/27.
 */

public class Constans {
    /**
     * EventBus 的消息
     */
    public static final int LOGIN_MESSAGE = 1000;  // 登录
    public static final int SALTTIME_MESSAGE = 1001; // 握手认证失效
    public static final int CHANGE_TITLE = 1002; // 改变标题栏的文字
    /**
     * 极光推送字段
     */
    public static final String EXTRA_EXTRA_NAME = "P_CUSTOMER_NUM";
    public static final String EXTRA_EXTRA_VALUE = "EXTRA_EXTRA_VALUE";
    // 标记是否是第一次打开MainACtivity,默认为true
    public static boolean isFirstOpenMainActivity = true;
    //  应用终端名
    public static String appname = "android";
    // 应用终端版本
    public static String ver = "v1.0.0";
    // 应用密匙串
    public static String api_key = "huitian_o+!Gks9=_android";
    // 一次握手参数
    public static String s = "";
    // 随机生成的字符串
    public static String r = "";
    // m = z   服务器通过客户端随机字符串生成的校验密串
    public static String m = "";
    // 通过接口返回的数据字段y进行base64反解码
    public static String n = "";
    // 通过接口返回的数据字段x进行base64反解码
    public static String t = "";
    // 二次认证密匙k
    public static String k = "";
    // 免密登陆字段
    public static String keyStr = "key_str";
    // SharePreference名称
    public static final String SP_NAME = "config";
    // 过期时间戳
    public static long expire = 0;
    // 保存在sp里面的字段名
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String M = "m";
    public static final String N = "n";
    public static final String T = "t";
    public static final String K = "k";
    public static final String EXPIRE_TIME = "expire";
    public static final String USER_NICK_NAME = "userNiceName";
    public static final String REGISTRATIONID = "c";
    // webview类型
    public static final String WEBVIEW_TYPE = "webviewType";
    // 保存cookie到本地的SP名称
    public static final String COOKIE_PREF = "cookies_prefs";
    /**
     * 打开webview的Type类型
     */
    public static final int STOCK_SEACH = 10000; // 库存查询
    public static final int DELIVER_SEACH = 10001; // 发货查询
    public static final int LOGISTICS_SEACH = 10002; // 物流查询
    public static final int AGGREGATE_SEACH = 10003; // 汇总查询
    public static final int CREDITOR_SEACH = 10004; // 债权查询
    public static final int RISK_SEACH = 10005; // 债权提醒
    public static final int RISK_LIST_SEACH = 10006; // 债权提醒列表

}
