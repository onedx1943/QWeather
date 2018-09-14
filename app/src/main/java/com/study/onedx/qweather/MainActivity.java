package com.study.onedx.qweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.study.onedx.qweather.bean.DayForecast;
import com.study.onedx.qweather.bean.HourForecast;
import com.study.onedx.qweather.gson.forecast.Daily;
import com.study.onedx.qweather.gson.forecast.ForecastWeather;
import com.study.onedx.qweather.gson.forecast.Hourly;
import com.study.onedx.qweather.gson.realTime.RealTimeWeather;
import com.study.onedx.qweather.utils.HttpUtil;
import com.study.onedx.qweather.utils.Utility;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final static int ACCESS_COARSE_LOCATION_REQUEST_CODE = 1;
    private final static String CAIYUN_KEY = "qgxSWjkH1b03Ttdp";

    private RelativeLayout weatherLayout;

    private TextView messageText;
    private TextView location;
    private TextView refreshTime;
    private TextView temperature;
    private TextView skyCon;
    private TextView realTimePm25;

    private ListView mDailyForecastList;
    private RecyclerView mHourlyForecastList;

    private DailyForecastAdapter dailyForecastAdapter;
    private HourlyForecastAdapter hourlyForecastAdapter;

    private List<DayForecast> dayForecastList = new ArrayList<>();
    private List<HourForecast> hourForecastList = new ArrayList<>();

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private String lon = null;//经度
    private String lat = null;//纬度
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        weatherLayout = findViewById(R.id.layout_weather);
        weatherLayout.setVisibility(View.INVISIBLE);
        //messageText = findViewById(R.id.message_text);
        location = findViewById(R.id.location);
        refreshTime = findViewById(R.id.refresh_time);
        temperature = findViewById(R.id.temperature);
        skyCon = findViewById(R.id.sky_con);
        realTimePm25 = findViewById(R.id.real_time_pm25);
        mDailyForecastList = findViewById(R.id.lv_forecast_day);
        mHourlyForecastList = findViewById(R.id.rec_forecast_hour);
        dailyForecastAdapter = new DailyForecastAdapter(MainActivity.this, R.layout.item_forecast_daily, dayForecastList);
        mDailyForecastList.setAdapter(dailyForecastAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHourlyForecastList.setLayoutManager(layoutManager);
        hourlyForecastAdapter= new HourlyForecastAdapter(hourForecastList);
        mHourlyForecastList.setAdapter(hourlyForecastAdapter);

        obtainLocation();
    }

    /**
     * 获取位置信息
     */
    private void obtainLocation(){
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

    /**
     * 调用高德地图SDK
     */
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
        mLocationOption.setNeedAddress(true);
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
                if (aMapLocation.getErrorCode() == 0 && aMapLocation.getDistrict() != null) {
                    //解析aMapLocation获取相应内容。纬度范围-90~90，经度范围-180~180
                    //格式化参数：保留四位小数
                    lat = new DecimalFormat("0.0000").format(aMapLocation.getLatitude());
                    lon = new DecimalFormat("0.0000").format(aMapLocation.getLongitude());
                    //aMapLocation.getProvince();//省
                    //aMapLocation.getCity();//市
                    address = aMapLocation.getDistrict();//区

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

    /**
     * 根据经纬度获取实时天气情况
     */
    private void getWeatherInfo(){
        if(lat == null || lon == null){
            Toast.makeText(MainActivity.this, "获取位置信息失败", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "https://api.caiyunapp.com/v2/" + CAIYUN_KEY + "/" +
                lon + "," + lat + "/realtime.json";
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

                final RealTimeWeather realTimeWeather = Utility.handleRealTimeResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //messageText.setText(responseText);
                        showWeatherInfo(realTimeWeather);
                    }
                });
                getForecastInfo();
            }
        });
    }

    /**
     * 获取天气预报
     */
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
                final ForecastWeather forecastWeather = Utility.handleForecastResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //messageText.setText(responseText);
                        showForecast(forecastWeather);
                    }
                });
            }
        });
    }

    /**
     * 展示天气信息
     * @param realTimeWeather 实时天气
     */
    private void showWeatherInfo(RealTimeWeather realTimeWeather){
        //todo 展示天气信息
        location.setText(address);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        String realTime = "更新时间：" + sdf.format(realTimeWeather.server_time * 1000);
        refreshTime.setText(realTime);
        String realTimeTemperature = String.valueOf(Math.round(realTimeWeather.result.temperature)) + "°";
        temperature.setText(realTimeTemperature);
        String realStatus = getSkyCon(realTimeWeather.result.skycon);
        skyCon.setText(realStatus);
        String pm25 = "空气" + getPM25(realTimeWeather.result.pm25) + " " + realTimeWeather.result.pm25;
        realTimePm25.setText(pm25);

        hourForecastList.clear();
        HourForecast now = new HourForecast();
        now.setTime("现在");
        now.setSkyInfo(realStatus);
        now.setTemperature(String.valueOf(Math.round(realTimeWeather.result.temperature)));
        hourForecastList.add(now);
    }

    /**
     * 展示天气预报信息
     * @param forecastWeather 预报信息
     */
    private void showForecast(ForecastWeather forecastWeather){
        //todo 展示天气预报
        Daily daily = forecastWeather.result.daily;
        if("ok".equals(daily.status)){
            dayForecastList.clear();
            //未来五天天气预报
            for (int i = 0; i < 5; i++){
                DayForecast dayForecast = new DayForecast();
                dayForecast.setDate(dateToWeek(daily.skyconList.get(i).date));
                dayForecast.setSkyInfo(getSkyCon(daily.skyconList.get(i).value));
                dayForecast.setMaxTemp(String.valueOf(Math.round(Double.parseDouble(daily.temperatureList.get(i).max))));
                dayForecast.setMinTemp(String.valueOf(Math.round(Double.parseDouble(daily.temperatureList.get(i).min))));
                dayForecastList.add(dayForecast);
            }
            dailyForecastAdapter.notifyDataSetChanged();
            mDailyForecastList.getHeight();
            mDailyForecastList.getLayoutParams();
            Utility.setListViewHeightBasedOnChildren(mDailyForecastList);
        }
        Hourly hourly = forecastWeather.result.hourly;
        if("ok".equals(hourly.status)){
            //未来24小时天气预报
            for(int i = 0; i < 24; i++){
                HourForecast hourForecast = new HourForecast();
                hourForecast.setTime(dateToHour(hourly.skyconList.get(i).datetime));
                hourForecast.setSkyInfo(getSkyCon(hourly.skyconList.get(i).value));
                hourForecast.setTemperature(String.valueOf(Math.round(Double.parseDouble(hourly.temperatureList.get(i).value))));
                hourForecastList.add(hourForecast);
            }
            hourlyForecastAdapter.notifyDataSetChanged();
        }

        weatherLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 根据天气代码装换为天气信息
     * @param skyCon 天气代码
     * @return 天气信息
     */
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

    /**
     * PM2.5值装换为空气质量等级
     * @param pm25 PM2.5值
     * @return 空气质量等级
     */
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
        } else {
            return "严重污染";
        }
    }

    /**
     * 将字符串日期转换为星期
     * @param strDate 日期
     * @return 星期
     */
    private String dateToWeek(String strDate){
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sDateFormat1=new SimpleDateFormat("MM月dd日", Locale.US);
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date date = null;
        String date1 = null;
        try {
            date = sDateFormat.parse(strDate);
            date1 = sDateFormat1.format(date);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0){
            w = 0;
        }
        return weekDays[w] + " (" + date1 + ")";
    }

    /**
     * 将字符串日期转换为小时
     * @param strDate 日期
     * @return 时
     */
    private String dateToHour(String strDate){
        SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        SimpleDateFormat sDateFormat1=new SimpleDateFormat("HH", Locale.US);
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date date = null;
        String date1 = null;
        try {
            date = sDateFormat.parse(strDate);
            date1 = sDateFormat1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0){
            w = 0;
        }
        return date1 + "时";
    }
}
