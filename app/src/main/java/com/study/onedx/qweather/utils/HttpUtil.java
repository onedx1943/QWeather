package com.study.onedx.qweather.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    /**
     * 发送http请求
     * @param url 请求地址
     * @param callback 回调函数
     */
    public static void sendOkHttpRequest(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }


}
