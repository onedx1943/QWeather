package com.study.onedx.qweather.gson.realTime;

import com.google.gson.annotations.SerializedName;

/**
 * 降水
 */
public class Precipitation {

    //最近的降水带
    @SerializedName("nearest")
    public Nearest nearest;

    //本地的降水
    @SerializedName("local")
    public Local local;

    public class Nearest {

        @SerializedName("status")
        public String status;
        //距离
        @SerializedName("distance")
        public double distance;
        //降水强度
        @SerializedName("intensity")
        public double intensity;
    }

    public class Local {
        @SerializedName("status")
        public String status;
        //降水强度
        @SerializedName("intensity")
        public double intensity;
        //数据源
        @SerializedName("datasource")
        public String datasource;
    }
}
