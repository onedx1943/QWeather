package com.study.onedx.qweather.bean;

public class HourForecast {

    private String time;
    private String skyInfo;
    private String temperature;

    public HourForecast() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSkyInfo() {
        return skyInfo;
    }

    public void setSkyInfo(String skyInfo) {
        this.skyInfo = skyInfo;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
