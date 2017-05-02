package com.example.chen.oamanager.api;


import com.example.chen.oamanager.bean.HuiTianResponse;
import com.example.chen.oamanager.bean.LoginBean;
import com.example.chen.oamanager.bean.MeizhiData;
import com.example.chen.oamanager.bean.SalttimeBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * des:ApiService
 * Created by xsf
 * on 2016.06.15:47
 */
public interface ApiService {
    @GET("data/福利/{size}/{page}")
    Observable<MeizhiData> getPhotoList(
            @Header("Cache-Control") String cacheControl,
            @Path("size") int size,
            @Path("page") int page);

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
            @Field("key_str") String key_str);

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

}
