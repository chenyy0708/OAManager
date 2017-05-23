package com.huitian.oamanager.api;


import com.huitian.oamanager.bean.GenCodeBean;
import com.huitian.oamanager.bean.HuiTianResponse;
import com.huitian.oamanager.bean.LoginBean;
import com.huitian.oamanager.bean.PaymentZhaiQuanBean;
import com.huitian.oamanager.bean.SalttimeBean;
import com.huitian.oamanager.bean.YMDSales;
import com.huitian.oamanager.bean.ZQCountBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * des:ApiService
 * Created by xsf
 * on 2016.06.15:47
 */
public interface ApiService {
    /**
     * 用户登陆
     *
     * @param cacheControl
     * @param m
     * @param n
     * @param t
     * @param username
     * @param pass
     * @param k
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Observable<HuiTianResponse<LoginBean>> getLoginUser(
            @Header("Cache-Control") String cacheControl,
            @Header("m") String m,
            @Header("n") String n,
            @Header("t") String t,
            @Field("username") String username,
            @Field("pass") String pass,
            @Field("k") String k,
            @Field("key_str") String key_str,
            @Field("registrationID") String registrationID);


    /**
     * 用户登陆
     *
     * @param cacheControl
     * @param m
     * @param n
     * @param t
     * @param username
     * @param pass
     * @param k
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Call<HuiTianResponse<LoginBean>> getLoginUserForCall(
            @Header("Cache-Control") String cacheControl,
            @Header("m") String m,
            @Header("n") String n,
            @Header("t") String t,
            @Field("username") String username,
            @Field("pass") String pass,
            @Field("k") String k,
            @Field("key_str") String key_str,
            @Field("registrationID") String registrationID);

    /**
     * 进行一次握手认证
     *
     * @param cacheControl
     * @param s            一次认证计算的认证加密串结果
     * @param r            加密认证密串时采用的随机字符串
     * @return
     */
    @FormUrlEncoded
    @POST("User/salttime")
    Observable<HuiTianResponse<SalttimeBean>> getSalttime(
            @Header("Cache-Control") String cacheControl,
            @Field("s") String s,
            @Field("r") String r
    );


    /**
     * 进行一次握手认证
     *
     * @param cacheControl
     * @param s            一次认证计算的认证加密串结果
     * @param r            加密认证密串时采用的随机字符串
     * @return
     */
    @FormUrlEncoded
    @POST("User/salttime")
    Call<HuiTianResponse<SalttimeBean>> getSalttimeForCall(
            @Header("Cache-Control") String cacheControl,
            @Field("s") String s,
            @Field("r") String r
    );

    /**
     * 退出登陆
     *
     * @param cacheControl
     * @param k
     * @return
     */
    @FormUrlEncoded
    @POST("user/loginout")
    Observable<HuiTianResponse<String>> loginOut(
            @Header("Cache-Control") String cacheControl,
            @Header("m") String m,
            @Header("n") String n,
            @Header("t") String t,
            @Field("k") String k
    );

    /**
     * 获取验证码
     *
     * @param cacheControl
     * @param k
     * @return
     */
    @FormUrlEncoded
    @POST("User/genCode")
    Observable<HuiTianResponse<GenCodeBean>> genCode(
            @Header("Cache-Control") String cacheControl,
            @Header("m") String m,
            @Header("n") String n,
            @Header("t") String t,
            @Field("phone") String phone,
            @Field("k") String k
    );

    /**
     * 忘记密码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("User/forgetPassword")
    Observable<HuiTianResponse<String>> forgetPassword(
            @Header("Cache-Control") String cacheControl,
            @Header("m") String m,
            @Header("n") String n,
            @Header("t") String t,
            @Field("k") String k,
            @Field("phone") String phone,
            @Field("code") String code,
            @Field("password") String password,
            @Field("confirmPassword") String confirmPassword
    );

    /**
     * 修改密码
     *
     * @param cacheControl
     * @param m
     * @param n
     * @param t
     * @param k
     * @param username
     * @param oldpass
     * @param newpass
     * @return
     */
    @FormUrlEncoded
    @POST("user/changepass")
    Observable<HuiTianResponse<String>> changePassword(
            @Header("Cache-Control") String cacheControl,
            @Header("m") String m,
            @Header("n") String n,
            @Header("t") String t,
            @Field("k") String k,
            @Field("username") String username,
            @Field("oldpass") String oldpass,
            @Field("newpass") String newpass);


    /**
     * 销售额
     *
     * @param cacheControl
     * @param m
     * @param n
     * @param t
     * @param k
     * @return
     */
    @FormUrlEncoded
    @POST("report/getYMDSales")
    Observable<HuiTianResponse<YMDSales>> getYMDSales(
            @Header("Cache-Control") String cacheControl,
            @Header("m") String m,
            @Header("n") String n,
            @Header("t") String t,
            @Field("k") String k
    );


    /**
     * 债权和回款
     *
     * @param cacheControl
     * @param m
     * @param n
     * @param t
     * @param k
     * @return
     */
    @FormUrlEncoded
    @POST("report/getCreditInfo")
    Observable<HuiTianResponse<PaymentZhaiQuanBean>> getPaymentAndZhaiQuan(
            @Header("Cache-Control") String cacheControl,
            @Header("m") String m,
            @Header("n") String n,
            @Header("t") String t,
            @Field("k") String k
    );


    /**
     * 风险债权个数
     *
     * @param cacheControl
     * @param m
     * @param n
     * @param t
     * @param k
     * @return
     */
    @FormUrlEncoded
    @POST("Proc/PROC_QUERY_RISK_DEFINE")
    Observable<HuiTianResponse<ZQCountBean>> getZQCount(
            @Header("Cache-Control") String cacheControl,
            @Header("m") String m,
            @Header("n") String n,
            @Header("t") String t,
            @Field("k") String k,
            @Field("P_CURR_USER_ID") String userName
    );

}
