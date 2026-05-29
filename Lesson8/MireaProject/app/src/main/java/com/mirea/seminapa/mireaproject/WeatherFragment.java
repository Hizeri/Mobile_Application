package com.mirea.seminapa.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherFragment extends Fragment {

    private TextView textViewStatus;
    private TextView textViewTemperature;
    private TextView textViewWindSpeed;
    private TextView textViewWindDirection;
    private Button buttonLoadWeather;

    // Ссылка на сервис погоды Open-Meteo для Москвы
    private static final String WEATHER_URL =
            "https://api.open-meteo.com/v1/forecast?latitude=55.75&longitude=37.62&current_weather=true";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        textViewStatus = view.findViewById(R.id.textViewWeatherStatus);
        textViewTemperature = view.findViewById(R.id.textViewTemperature);
        textViewWindSpeed = view.findViewById(R.id.textViewWindSpeed);
        textViewWindDirection = view.findViewById(R.id.textViewWindDirection);
        buttonLoadWeather = view.findViewById(R.id.buttonLoadWeather);
        buttonLoadWeather.setOnClickListener(v -> loadWeather());

        return view;
    }

    private void loadWeather() {
        textViewStatus.setText("Статус: загружаем погоду...");

        // Сетевой запрос выполняем в отдельном потоке
        new Thread(() -> {
            try {
                String json = downloadUrl(WEATHER_URL);

                JSONObject rootObject = new JSONObject(json);
                JSONObject currentWeather = rootObject.getJSONObject("current_weather");

                double temperature = currentWeather.optDouble("temperature");
                double windSpeed = currentWeather.optDouble("windspeed");
                double windDirection = currentWeather.optDouble("winddirection");

                requireActivity().runOnUiThread(() -> {
                    textViewStatus.setText("Статус: данные получены");

                    textViewTemperature.setText("Температура: " + temperature + " °C");
                    textViewWindSpeed.setText("Скорость ветра: " + windSpeed + " км/ч");
                    textViewWindDirection.setText("Направление ветра: " + windDirection + "°");
                });

            } catch (Exception e) {
                e.printStackTrace();

                requireActivity().runOnUiThread(() -> {
                    textViewStatus.setText("Статус: ошибка загрузки");
                    Toast.makeText(getContext(), "Ошибка получения погоды", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private String downloadUrl(String address) throws Exception {
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(100000);
            connection.setReadTimeout(100000);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                int read;
                while ((read = inputStream.read()) != -1) {
                    byteArrayOutputStream.write(read);
                }

                byteArrayOutputStream.close();

                return byteArrayOutputStream.toString();
            } else {
                return "{}";
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}