package com.study.onedx.qweather.gson.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hourly {

    @SerializedName("status")
    public String status;

    @SerializedName("pm25")
    public List<pm25> pm25List;

    @SerializedName("skycon")
    public List<skycon>  skyconList;

    @SerializedName("cloudrate")
    public List<cloudRate> cloudRateList;

    @SerializedName("aqi")
    public List<aqi> aqiList;

    @SerializedName("humidity")
    public List<humidity> humidityList;

    @SerializedName("precipitation")
    public List<precipitation> precipitationList;

    @SerializedName("wind")
    public List<wind> windList;

    @SerializedName("temperature")
    public List<temperature> temperatureList;

    public class pm25{

        @SerializedName("datetime")
        public String datetime;

        @SerializedName("value")
        public String value;
    }

    public class skycon{

        @SerializedName("datetime")
        public String datetime;

        @SerializedName("value")
        public String value;
    }

    public class cloudRate{

        @SerializedName("datetime")
        public String datetime;

        @SerializedName("value")
        public String value;
    }

    public class aqi{

        @SerializedName("datetime")
        public String datetime;

        @SerializedName("value")
        public String value;
    }

    public class humidity{

        @SerializedName("datetime")
        public String datetime;

        @SerializedName("value")
        public String value;
    }

    public class precipitation{

        @SerializedName("datetime")
        public String datetime;

        @SerializedName("value")
        public String value;
    }

    public class wind{

        @SerializedName("datetime")
        public String datetime;

        @SerializedName("speed")
        public String speed;

        @SerializedName("direction")
        public String direction;
    }

    public class temperature{

        @SerializedName("datetime")
        public String datetime;

        @SerializedName("value")
        public String value;
    }

}
