package com.example.weaterpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<WeatherData> weatherDataList;

    public WeatherAdapter(Context context, ArrayList<WeatherData> weatherDataList) {
        this.context = context;
        this.weatherDataList = weatherDataList;
    }

    @Override
    public int getCount() {
        return weatherDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
        }

        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView temperatureTextView = convertView.findViewById(R.id.temperatureTextView);
        TextView apparentTemperatureTextView = convertView.findViewById(R.id.apparentTemperatureTextView);
        TextView rainTextView = convertView.findViewById(R.id.rainTextView);
        TextView surfacePressureTextView = convertView.findViewById(R.id.surfacePressureTextView);

        WeatherData weatherData = weatherDataList.get(position);

        dateTextView.setText(weatherData.getDate());
        temperatureTextView.setText(weatherData.getTemperature());
        apparentTemperatureTextView.setText(weatherData.getApparentTemperature());
        rainTextView.setText(weatherData.getRain());
        surfacePressureTextView.setText(weatherData.getSurfacePressure());

        return convertView;
    }
}
