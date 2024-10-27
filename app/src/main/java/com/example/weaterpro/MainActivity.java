package com.example.weaterpro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etCity;
    private ListView listViewWeather;
    private WeatherAdapter weatherAdapter;
    private ArrayList<WeatherData> weatherDataList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnClear = findViewById(R.id.btnClear);
        listViewWeather = findViewById(R.id.listViewWeather);

        weatherDataList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this, weatherDataList);
        listViewWeather.setAdapter(weatherAdapter);

        btnSearch.setOnClickListener(v -> fetchWeatherData());
        btnClear.setOnClickListener(v -> clearWeatherData());
    }

    private void fetchWeatherData() {
        String city = etCity.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, введите город", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL для получения геолокации по названию города
        String locationUrl = "https://nominatim.openstreetmap.org/search?city=" + city + "&format=json";

        new Thread(() -> {
            try {
                URL url = new URL(locationUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Парсинг ответа и получение координат
                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    JSONObject locationData = jsonArray.getJSONObject(0);
                    String latitude = locationData.getString("lat");
                    String longitude = locationData.getString("lon");

                    // Формирование ссылки для API погоды
                    String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                            "&longitude=" + longitude +
                            "&current=temperature_2m&hourly=temperature_2m,apparent_temperature,rain,surface_pressure&wind_speed_unit=ms";

                    // Вызов API погоды
                    fetchWeather(weatherUrl);
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Город не найден", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void fetchWeather(String weatherUrl) {
        new Thread(() -> {
            try {
                URL url = new URL(weatherUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                parseWeatherData(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void parseWeatherData(String jsonResponse) {
        runOnUiThread(() -> {
            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject hourly = jsonObject.getJSONObject("hourly");
                JSONArray timeArray = hourly.getJSONArray("time"); // Новый массив времени
                JSONArray temperatures = hourly.getJSONArray("temperature_2m");
                JSONArray apparentTemperatures = hourly.getJSONArray("apparent_temperature");
                JSONArray rains = hourly.getJSONArray("rain");
                JSONArray pressures = hourly.getJSONArray("surface_pressure");

                // Очистка предыдущих данных
                weatherDataList.clear();

                // Добавление новых данных
                for (int i = 0; i < temperatures.length(); i++) {
                    WeatherData weatherData = new WeatherData(
                            timeArray.getString(i),
                            temperatures.getString(i) + "°C",
                            apparentTemperatures.getString(i) + "°C",
                            rains.getString(i) + "мм",
                            pressures.getString(i) + "гПа"
                    );
                    weatherDataList.add(weatherData);
                }

                weatherAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void clearWeatherData() {
        etCity.setText("");
        weatherDataList.clear();
        weatherAdapter.notifyDataSetChanged();
    }
}
