package com.study.onedx.qweather.gson.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Daily {

    @SerializedName("status")
    public String status;

    @SerializedName("temperature")
    public List<temperature> temperatureList;

    @SerializedName("skycon")
    public List<skycon> skyconList;

    public class temperature {

        @SerializedName("date")
        public String date;

        @SerializedName("max")
        public String max;

        @SerializedName("avg")
        public String avg;

        @SerializedName("min")
        public String min;
    }

    public class skycon{

        @SerializedName("date")
        public String date;

        @SerializedName("value")
        public String value;
    }

    //pm2.5

    //日出日落

    // 云量

    // AQI

    // 降水强度

    // 风

    // 相对湿度
}
