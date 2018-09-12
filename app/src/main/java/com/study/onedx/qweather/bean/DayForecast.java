package com.study.onedx.qweather.bean;

public class DayForecast {

    private String date;
    private String skyInfo;
    private String maxTemp;
    private String minTemp;

    public DayForecast() {
    }

    public DayForecast(String date, String skyInfo, String maxTemp, String minTemp) {
        this.date = date;
        this.skyInfo = skyInfo;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSkyInfo() {
        return skyInfo;
    }

    public void setSkyInfo(String skyInfo) {
        this.skyInfo = skyInfo;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }
}
