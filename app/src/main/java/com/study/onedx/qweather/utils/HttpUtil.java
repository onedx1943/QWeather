package com.study.onedx.qweather.utils;

import com.google.gson.Gson;
import com.study.onedx.qweather.gson.realTime.RealTimeWeather;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    public static void sendOkHttpRequest(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

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
}
