package com.study.onedx.qweather.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.study.onedx.qweather.gson.forecast.ForecastWeather;
import com.study.onedx.qweather.gson.realTime.RealTimeWeather;

import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

    /**
     * 处理实时天气信息
     *
     * @param response 返回值
     * @return 实时天气对象
     */
    public static RealTimeWeather handleRealTimeResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String responseText = jsonObject.toString();
            return new Gson().fromJson(responseText, RealTimeWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理天气预报信息
     *
     * @param response 返回值
     * @return 天气预报对象
     */
    public static ForecastWeather handleForecastResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String responseText = jsonObject.toString();
            return new Gson().fromJson(responseText, ForecastWeather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
