package com.study.onedx.qweather.gson.realTime;

import com.google.gson.annotations.SerializedName;

/**
 * 实时天气
 */
public class RealTimeWeather {

    @SerializedName("status")
    public String status;

    @SerializedName("server_time")
    public long server_time;

    @SerializedName("result")
    public Result result;

    public class Result {
        @SerializedName("status")
        public String status;

        @SerializedName("temperature")
        public double temperature;

        //天气概况
        @SerializedName("skycon")
        public String skycon;

        @SerializedName("pm25")
        public int pm25;

        // 云量
        @SerializedName("cloudrate")
        public double cloudrate;

        //相对湿度
        @SerializedName("humidity")
        public double humidity;

        @SerializedName("precipitation")
        public Precipitation precipitation;

        @SerializedName("wind")
        public Wind wind;

    }

}
