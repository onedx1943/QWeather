package com.study.onedx.qweather.gson.realTime;

import com.google.gson.annotations.SerializedName;

//风
public class Wind {

    //风向。单位是度。正北方向为0度，顺时针增加到360度。
    @SerializedName("direction")
    public double direction;

    //风速，米制下是公里每小时
    @SerializedName("speed")
    public double speed;

}
