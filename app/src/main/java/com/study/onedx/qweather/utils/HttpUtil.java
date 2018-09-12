package com.study.onedx.qweather.utils;

import com.google.gson.Gson;
import com.study.onedx.qweather.gson.forecast.ForecastWeather;
import com.study.onedx.qweather.gson.realTime.RealTimeWeather;

import org.json.JSONException;
import org.json.JSONObject;

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

    /**
     * 处理实时天气信息返回值
     * @param response 返回值
     * @return 实时天气对象
     */
    public static RealTimeWeather handleRealTimeResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String responseText = jsonObject.toString();
            return new Gson().fromJson(responseText, RealTimeWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ForecastWeather handleForecastResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            String responseText = jsonObject.toString();
            return new Gson().fromJson(responseText, ForecastWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
