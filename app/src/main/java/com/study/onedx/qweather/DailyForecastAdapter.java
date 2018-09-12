package com.study.onedx.qweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.study.onedx.qweather.bean.DayForecast;

import java.util.List;

public class DailyForecastAdapter extends ArrayAdapter<DayForecast> {

    private int resourceId;

    public DailyForecastAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<DayForecast> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DayForecast dayForecast = getItem(position);
        DailyItem dailyItem;
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            dailyItem = new DailyItem();
            dailyItem.dateText = view.findViewById(R.id.txt_date);
            dailyItem.infoText = view.findViewById(R.id.txt_info);
            dailyItem.maxText = view.findViewById(R.id.txt_temp_max);
            dailyItem.minText = view.findViewById(R.id.txt_temp_min);
            view.setTag(dailyItem);
        } else {
            view = convertView;
            dailyItem = (DailyItem) view.getTag();
        }
        dailyItem.dateText.setText(dayForecast.getDate());
        dailyItem.infoText.setText(dayForecast.getSkyInfo());
        dailyItem.maxText.setText(dayForecast.getMaxTemp());
        dailyItem.minText.setText(dayForecast.getMinTemp());
        return view;
    }

    class DailyItem{
        TextView dateText;
        TextView infoText;
        TextView maxText;
        TextView minText;
    }
}
