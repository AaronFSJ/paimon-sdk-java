package com.dreamkey.paimon.util;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.StaticConstant;
import com.dreamkey.paimon.exception.PaimonException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


/**
 * @author: Aaron
 * @description:
 * @date: Create in 8:25 PM 2022/3/17
 * @modified by:
 */
public class OkHttpUtil {

    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;
    private Map<String, String> headerMap;
    private Map<String, String> paramMap;
    private String body;


    private String url;
    private Request.Builder request;

    /**
     * 初始化okHttpClient，并且允许https访问
     */
    private OkHttpUtil() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtil.class) {
                if (okHttpClient == null) {
                    TrustManager[] trustManagers = buildTrustManagers();
                    okHttpClient = new OkHttpClient.Builder()
                            .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
                            .hostnameVerifier((hostName, session) -> true)
                            .retryOnConnectionFailure(true)
                            .build();
                    addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
                }
            }
        }
    }

    /**
     * 用于异步请求时，控制访问线程数，返回结果
     *
     * @return
     */
    private static Semaphore getSemaphoreInstance() {
        //只能1个线程同时访问
        synchronized (OkHttpUtil.class) {
            if (semaphore == null) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }

    /**
     * 创建OkHttpUtils
     *
     * @return
     */
    public static OkHttpUtil builder() {
        return new OkHttpUtil();
    }

    /**
     * 添加url
     *
     * @param url
     * @return
     */
    public OkHttpUtil url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 添加参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtil addParam(String key, String value) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    public OkHttpUtil addParams(Map<String, String> params) {
        paramMap = params;
        return this;
    }

    public OkHttpUtil addBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   参数名
     * @param value 参数值
     * @return
     */
    public OkHttpUtil addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    public OkHttpUtil addHeaders(Map<String, String> headers) {
        headerMap = headers;
        return this;
    }

    /**
     * 初始化get方法
     *
     * @return
     */
    private OkHttpUtil get() {
        request = new Request.Builder().get();
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8)).
                            append("=").
                            append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8)).
                            append("&");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        request.url(urlBuilder.toString());
        return this;
    }

    /**
     * 初始化post方法
     *
     * @param isJsonPost true等于json的方式提交数据，类似postman里post方法的raw
     * false等于普通的表单提交
     * @return
     */
    public static final MediaType mediaType
            = MediaType.get("application/json; charset=utf-8");

    /**
     * 请求体处理
     *
     * @param isJsonPost
     * @return
     */
    private RequestBody bodyDeal(boolean isJsonPost) {
        if (isJsonPost && !StringUtils.isEmpty(body)) {
            return RequestBody.create(body, mediaType);
        }
        return RequestBody.create("", mediaType);

    }

    private OkHttpUtil post(boolean isJsonPost) {
        request = new Request.Builder()
                .post(bodyDeal(isJsonPost))
                .url(url);
        return this;
    }

    private OkHttpUtil put(boolean isJsonPost) {
        request = new Request.Builder().put(bodyDeal(isJsonPost)).url(url);
        return this;
    }


    private OkHttpUtil delete(boolean isJsonPost) {
        request = new Request.Builder().delete(bodyDeal(isJsonPost)).url(url);
        return this;
    }

    /**
     * 同步请求
     *
     * @return
     */
    private ResponseEntity sync() {
        setHeader(request);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            if (StaticConstant.HTTP_STATUS_OK != response.code()) {
                throw new PaimonException(response.message());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new PaimonException("Response is null");
            }

            String bodyStr = body.string();

            ResponseEntity entity = JSONObject.parseObject(bodyStr, ResponseEntity.class);
            if (!StaticConstant.ERROR_CODE_OK.equals(entity.getErrorCode()) || entity.getMessage().length() != 0) {
                throw new PaimonException(entity.getErrorCode(), entity.getMessage());
            }
            return entity;

        } catch (IOException e) {
            throw new PaimonException(e.getMessage());
        }
    }


    /**
     * 异步请求，有返回值
     */
    public String async() {
        StringBuilder buffer = new StringBuilder("");
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                buffer.append("请求出错：").append(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                buffer.append(Objects.requireNonNull(response.body()).string());
                getSemaphoreInstance().release();
            }
        });
        try {
            getSemaphoreInstance().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 异步请求，带有接口回调
     *
     * @param callBack
     */
    public void async(ICallBack callBack) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callBack.onFailure(call, e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                assert response.body() != null;
                callBack.onSuccessful(call, Objects.requireNonNull(response.body()).string());
            }
        });
    }

    /**
     * 为request添加请求头
     *
     * @param request
     */
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            try {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    /**
     * 自定义一个接口回调
     */
    public interface ICallBack {

        void onSuccessful(Call call, String data);

        void onFailure(Call call, String errorMsg);

    }

    /**
     * 执行 Post 方法
     *
     * @param url
     * @param headerMap
     * @param body
     * @return
     */
    public ResponseEntity doPost(String url, Map<String, String> headerMap, String body) {
        return OkHttpUtil.builder().url(url)
                .addHeaders(headerMap)
                .addBody(body)
                .post(true)
                .sync();
    }


    public ResponseEntity doPut(String url, Map<String, String> headerMap, String body) {
        return OkHttpUtil.builder().url(url)
                .addHeaders(headerMap)
                .addBody(body)
                .put(true)
                .sync();
    }

    public ResponseEntity doDelete(String url, Map<String, String> headerMap, String body) {
        return OkHttpUtil.builder().url(url)
                .addHeaders(headerMap)
                .addBody(body)
                .delete(true)
                .sync();
    }


    /**
     * 执行 Get 方法
     *
     * @param url
     * @param headerMap
     * @param paramMap
     * @return
     */
    public ResponseEntity doGet(String url, Map<String, String> headerMap, Map<String, String> paramMap) {
        return OkHttpUtil.builder().url(url)
                .addHeaders(headerMap)
                .addParams(paramMap)
                .get()
                .sync();
    }

    public <T> T convertDataStringToEntity(ResponseEntity entity, Class<T> clazz) {
        if (entity != null && !StringUtils.isEmpty(entity.getData())) {
            return JSONObject.parseObject(entity.getData(), clazz);
        }
        return null;
    }


}


