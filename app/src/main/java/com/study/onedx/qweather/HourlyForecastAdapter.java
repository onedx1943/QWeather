package com.study.onedx.qweather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.onedx.qweather.bean.HourForecast;
import com.study.onedx.qweather.gson.forecast.Hourly;

import java.util.List;

public class HourlyForecastAdapter extends RecyclerView.Adapter {

    private List<HourForecast> mHourList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtHourlyTime;
        TextView txtHourlyInfo;
        TextView txtHourlyTemp;


        public ViewHolder(View view){
            super(view);
            txtHourlyTime = view.findViewById(R.id.txt_hourly_time);
            txtHourlyInfo = view.findViewById(R.id.txt_hourly_info);
            txtHourlyTemp = view.findViewById(R.id.txt_hourly_temp);
        }
    }

    public HourlyForecastAdapter(List<HourForecast> hourList) {
        mHourList = hourList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast_hourly, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HourForecast hourForecast = mHourList.get(position);
        ((ViewHolder) holder).txtHourlyTime.setText(hourForecast.getTime());
        ((ViewHolder) holder).txtHourlyInfo.setText(hourForecast.getSkyInfo());
        ((ViewHolder) holder).txtHourlyTemp.setText(hourForecast.getTemperature());
    }

    @Override
    public int getItemCount() {
        return mHourList.size();
    }
}

