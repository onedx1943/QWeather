package com.study.onedx.qweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.study.onedx.qweather.gson.realTime.RealTimeWeather;
import com.study.onedx.qweather.utils.HttpUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final static int ACCESS_COARSE_LOCATION_REQUEST_CODE = 1;

    private final static String CAIYUN_KEY = "qgxSWjkH1b03Ttdp";

    private TextView messageText;
    private TextView location;
    private TextView refreshTime;
    private TextView temperature;
    private TextView skyCon;
    private TextView realTimePm25;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //经度
    private String lon = null;
    //纬度
    private String lat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageText = findViewById(R.id.message_text);
        location = findViewById(R.id.location);
        refreshTime = findViewById(R.id.refresh_time);
        temperature = findViewById(R.id.temperature);
        skyCon = findViewById(R.id.sky_con);
        realTimePm25 = findViewById(R.id.real_time_pm25);

        //必须获取定位权限才能获取天气信息
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        } else {
            //获取当前地点
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case ACCESS_COARSE_LOCATION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                } else {
                    Toast.makeText(MainActivity.this, "获取定位权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getLocation(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //true表示使用定位缓存策略；false表示不使用。
        mLocationOption.setLocationCacheEnable(false);
        //true表示定位返回经纬度同时返回地址描述（定位类型是网络定位的会返回）；false表示不返回地址描述。
        mLocationOption.setNeedAddress(false);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，
        // 启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocation(true);
        //mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 声明定位回调监听器
     */
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //解析aMapLocation获取相应内容。纬度范围-90~90，经度范围-180~180
                    //格式化参数：保留四位小数
                    lat = new DecimalFormat("0.0000").format(aMapLocation.getLatitude());
                    lon = new DecimalFormat("0.0000").format(aMapLocation.getLongitude());
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
            getWeatherInfo();
        }
    };

    private void getWeatherInfo(){
        if(lat == null || lon == null){
            Toast.makeText(MainActivity.this, "获取位置信息失败", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "https://api.caiyunapp.com/v2/" + CAIYUN_KEY + "/" +
                lon + "," + lat + "/realtime.json";

        //"https://api.caiyunapp.com/v2/TAkhjf8d1nlSlspN/121.6544,25.1552/forecast.json"
//        String url = "https://api.caiyunapp.com/v2/" + CAIYUN_KEY + "/" +
//                lon + "," + lat + "/forecast.json";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        //todo 执行刷新
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();

                final RealTimeWeather realTimeWeather = HttpUtil.handleRealTimeResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageText.setText(responseText);
                        showWeatherInfo(realTimeWeather);
                    }
                });
                getForecastInfo();
            }
        });
    }

    private void getForecastInfo(){
        String url = "https://api.caiyunapp.com/v2/" + CAIYUN_KEY + "/" +
                lon + "," + lat + "/forecast.json";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        //todo 执行刷新
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageText.setText(responseText);
                    }
                });
            }
        });
    }

    /**
     * 展示天气信息
     * @param realTimeWeather
     */
    private void showWeatherInfo(RealTimeWeather realTimeWeather){
        //todo 展示天气信息

        location.setText("南京市");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String realTime = "更新时间：" + sdf.format(realTimeWeather.server_time * 1000);
        refreshTime.setText(realTime);
        String realTimeTemperature = String.valueOf(Math.round(realTimeWeather.result.temperature)) + "°";
        temperature.setText(realTimeTemperature);
        String realStatus = getSkyCon(realTimeWeather.result.skycon);
        skyCon.setText(realStatus);
        String pm25 = "空气" + getPM25(realTimeWeather.result.pm25) + " " + realTimeWeather.result.pm25;
        realTimePm25.setText(pm25);

    }

    private String getSkyCon(String skyCon){
        if("CLEAR_DAY".equals(skyCon)){
            return "晴天";
        } else if("CLEAR_NIGHT".equals(skyCon)){
            return "晴夜";
        } else if("PARTLY_CLOUDY_DAY".equals(skyCon)){
            return "多云";
        } else if("PARTLY_CLOUDY_NIGHT".equals(skyCon)){
            return "多云";
        } else if("CLOUDY".equals(skyCon)){
            return "阴";
        } else if("RAIN".equals(skyCon)){
            return "雨";
        } else if("SNOW".equals(skyCon)){
            return "雪";
        } else if("WIND".equals(skyCon)){
            return "风";
        } else if("HAZE".equals(skyCon)){
            return "雾霾沙尘";
        }
        return "未知";
    }

    private String getPM25(int pm25){
        if(0 <= pm25 && pm25 <= 35){
            return "优";
        } else if( pm25 <=75){
            return "良";
        } else if( 115 >= pm25){
            return "轻度污染";
        } else if( 150 >= pm25){
            return "中度污染";
        } else if( 250 >= pm25){
            return "重度污染";
        } else if( pm25 >250){
            return "严重污染";
        }
        return "未知";
    }

}
