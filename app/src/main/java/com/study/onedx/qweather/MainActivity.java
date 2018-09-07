package com.study.onedx.qweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.study.onedx.qweather.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final static int INTERNET_REQUEST_CODE = 1;
    private final static int ACCESS_COARSE_LOCATION_REQUEST_CODE = 2;
    private final static int ACCESS_FINE_LOCATION_REQUEST_CODE = 3;

    private final static String KEY = "qgxSWjkH1b03Ttdp";

    private TextView messageText;

    //经度
    private String lon;
    //纬度
    private String lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageText = (TextView) findViewById(R.id.message_text);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.INTERNET},
//                    INTERNET_REQUEST_CODE);
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    ACCESS_FINE_LOCATION_REQUEST_CODE);
//        }


        //获取当前地点
        getLocation();
        //展示天气信息
        getWeatherInfo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case INTERNET_REQUEST_CODE:
                break;
            case ACCESS_COARSE_LOCATION_REQUEST_CODE:
                break;
            case ACCESS_FINE_LOCATION_REQUEST_CODE:
                break;
        }
    }

    private void getLocation(){
        //纬度范围-90~90，经度范围-180~180
        lat = "31.9701";
        lon = "118.8110";
    }

    private void getWeatherInfo(){
        if(lat == null || lon == null){
            Toast.makeText(MainActivity.this, "can't get location", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "https://api.caiyunapp.com/v2/" + KEY + "/" +
                lon + "," + lat + "/realtime.json";
        //"https://api.caiyunapp.com/v2/TAkhjf8d1nlSlspN/121.6544,25.1552/realtime.json"
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "get failure", Toast.LENGTH_SHORT).show();
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


    private void showWeatherInfo(String Weather){

    }

}
