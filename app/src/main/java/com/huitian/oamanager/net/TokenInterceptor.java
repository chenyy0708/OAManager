package com.huitian.oamanager.net;

import com.google.gson.Gson;
import com.huitian.oamanager.api.Api;
import com.huitian.oamanager.app.App;
import com.huitian.oamanager.app.Constans;
import com.huitian.oamanager.bean.HuiTianResponse;
import com.huitian.oamanager.bean.LoginBean;
import com.huitian.oamanager.bean.SalttimeBean;
import com.huitian.oamanager.util.MD5Utils;
import com.jaydenxiao.common.commonutils.SPUtils;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;

/**
 * Created by Chen on 2017/5/22.
 */

public class TokenInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static Gson gson;

    private boolean isRefreshSuccess = false;

    public TokenInterceptor() {
        gson = new Gson();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // try the request
        Response originalResponse = chain.proceed(request);

        /**通过如下的办法曲线取到请求完成的数据
         *
         * 原本想通过  originalResponse.body().string()
         * 去取到请求完成的数据,但是一直报错,不知道是okhttp的bug还是操作不当
         *
         * 然后去看了okhttp的源码,找到了这个曲线方法,取到请求完成的数据后,根据特定的判断条件去判断token过期
         */
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        String bodyString = buffer.clone().readString(charset);
        /***************************************/

        try {
            HuiTianResponse result = gson.fromJson(bodyString, HuiTianResponse.class);
            int code = result.getState();
            if (code == -1005 || code == -1015) { // 秘钥失效或者过期|
                isRefreshSuccess = false;
                synchronized (TokenInterceptor.class) {
                    if (!isRefreshSuccess) { // 如果握手没有成功才去握手
                        HuiTianResponse<SalttimeBean> newToken = getSalttime();
                        saveSalttime(newToken);
                        Request newrequest = getRequest(request, originalResponse);
                        return chain.proceed(newrequest);
                    } else { // 已经成功，用新的握手数据
                        Request newrequest = getRequest(request, originalResponse);
                        return chain.proceed(newrequest);
                    }
                }
            } else if (code == -1006) { // 登陆失败，重新登录
                Call<HuiTianResponse<LoginBean>> call = Api.getDefault().getLoginUserForCall(Api.getCacheControl(),
                        Constans.m, Constans.n, Constans.t, "", "", Constans.k,
                        SPUtils.getSharedStringData(App.getAppContext(), Constans.keyStr), SPUtils.getSharedStringData(App.getAppContext(), Constans.REGISTRATIONID));
                HuiTianResponse<LoginBean> loginBean = call.execute().body();
                saveLogin(loginBean);
                Request newrequest = getRequest(request, originalResponse);
                return chain.proceed(newrequest);
            }
        } catch (IOException e) {
            System.out.println("");
            e.printStackTrace();
        }
        return originalResponse;
    }

    private Request getRequest(Request request, Response originalResponse) {
        Request.Builder newRequestBuilder;
        newRequestBuilder = request.newBuilder();
        newRequestBuilder.header("m", Constans.m)
                .header("n", Constans.n)
                .header("t", Constans.t)
                .build();
        if (request.body() instanceof FormBody) {
            //创建一个新的body
            FormBody.Builder newFormBody = new FormBody.Builder();
            //取出旧的body
            FormBody oidFormBody = (FormBody) request.body();
            //遍历旧body 将旧body中的内容全部加入新body中
            for (int i = 0; i < oidFormBody.size(); i++) {
                //遍历旧body 取出旧body里面的k参数，换成新的k
                if (oidFormBody.encodedName(i).equals("k")) {
                    newFormBody.addEncoded(oidFormBody.encodedName(i), Constans.k);
                } else {
                    newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
                }
            }
            //构建新的请求
            newRequestBuilder.method(request.method(), newFormBody.build());
        }
        // 参数
        originalResponse.body().close();
        return newRequestBuilder.build();
    }

    private void saveLogin(HuiTianResponse<LoginBean> loginBean) {
        // 保存用户昵称
        SPUtils.setSharedStringData(App.getAppContext(), Constans.USER_NICK_NAME, loginBean.getData().getUser_info().getUSER_NAME());
        // 保存keystr信息，用于登陆失效免密登陆
        SPUtils.setSharedStringData(App.getAppContext(), Constans.keyStr, loginBean.getData().getKey_str());
        isRefreshSuccess = true;
    }

    private HuiTianResponse<SalttimeBean> getSalttime() throws IOException {
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
        Call<HuiTianResponse<SalttimeBean>> call = Api.getDefault().getSalttimeForCall(Api.getCacheControl(), Constans.s, Constans.r);
        //要用retrofit的同步方式
        return call.execute().body();
    }

    private void saveSalttime(HuiTianResponse<SalttimeBean> response) {
        // 客户端注意处理好提前进行一次认证接口自动调用和异常自动补偿认证逻辑以防接口认证失败
        // PHP的时间戳需要乘以1000才跟java获取的相同
        long expire = (long) response.getData().getExpire() * 1000L;
        long l = System.currentTimeMillis();
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
        SPUtils.setSharedStringData(App.getAppContext(), Constans.M, Constans.m);
        SPUtils.setSharedStringData(App.getAppContext(), Constans.N, Constans.n);
        SPUtils.setSharedStringData(App.getAppContext(), Constans.T, Constans.t);
        SPUtils.setSharedStringData(App.getAppContext(), Constans.K, Constans.k);
        // 时间戳保存到本地
        SPUtils.setSharedLongData(App.getAppContext(), Constans.EXPIRE_TIME, expire);
        isRefreshSuccess = true;
    }
}
