package com.study.onedx.qweather.gson.forecast;

import com.google.gson.annotations.SerializedName;

/**
 * 天气预报
 */
public class ForecastWeather {

    @SerializedName("status")
    public String status;

    @SerializedName("server_time")
    public long server_time;

    @SerializedName("result")
    public Result result;

    public class Result{
        @SerializedName("status")
        public String status;

        @SerializedName("hourly")
        public Hourly hourly;

        @SerializedName("minutely")
        public Minutely minutely;

        @SerializedName("daily")
        public Daily daily;

        @SerializedName("alert")
        public Alert alert;
    }
}
