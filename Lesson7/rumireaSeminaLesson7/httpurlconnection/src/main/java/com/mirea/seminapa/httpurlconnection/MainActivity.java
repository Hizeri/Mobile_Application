package com.mirea.seminapa.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HttpURLConnection";

    private Button buttonGetData;

    private TextView textViewStatus;
    private TextView textViewIp;
    private TextView textViewCity;
    private TextView textViewRegion;
    private TextView textViewCountry;
    private TextView textViewCoordinates;
    private TextView textViewWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим элементы интерфейса
        buttonGetData = findViewById(R.id.buttonGetData);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewIp = findViewById(R.id.textViewIp);
        textViewCity = findViewById(R.id.textViewCity);
        textViewRegion = findViewById(R.id.textViewRegion);
        textViewCountry = findViewById(R.id.textViewCountry);
        textViewCoordinates = findViewById(R.id.textViewCoordinates);
        textViewWeather = findViewById(R.id.textViewWeather);


        // По нажатию кнопки начинаем сетевой запрос
        buttonGetData.setOnClickListener(v -> {
            if (isInternetAvailable()) {
                loadIpAndWeather();
            } else {
                Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Проверка подключения к интернету
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void loadIpAndWeather() {
        // Показываем, что загрузка началась
        textViewStatus.setText("Статус: загружаем данные...");


        // Сетевые операции выполняем в отдельном потоке
        new Thread(() -> {
            try {
                // 1. Получаем JSON с IP и местоположением
                String ipInfoJson = downloadUrl("https://ipinfo.io/json");

                // 2. Разбираем JSON от ipinfo.io
                JSONObject ipObject = new JSONObject(ipInfoJson);

                String ip = ipObject.optString("ip", "нет данных");
                String city = ipObject.optString("city", "нет данных");
                String region = ipObject.optString("region", "нет данных");
                String country = ipObject.optString("country", "нет данных");
                String loc = ipObject.optString("loc", "");

                String latitude = "";
                String longitude = "";

                // loc приходит в виде "широта,долгота"
                if (!loc.isEmpty() && loc.contains(",")) {
                    String[] coordinates = loc.split(",");
                    latitude = coordinates[0];
                    longitude = coordinates[1];
                }

                // 3. По координатам получаем погоду
                String weatherText = "Погода: координаты не найдены";

                if (!latitude.isEmpty() && !longitude.isEmpty()) {
                    String weatherUrl =
                            "https://api.open-meteo.com/v1/forecast?latitude="
                                    + latitude
                                    + "&longitude="
                                    + longitude
                                    + "&current_weather=true";

                    String weatherJson = downloadUrl(weatherUrl);

                    // 4. Разбираем JSON погоды
                    JSONObject weatherObject = new JSONObject(weatherJson);
                    JSONObject currentWeather = weatherObject.getJSONObject("current_weather");

                    double temperature = currentWeather.optDouble("temperature");
                    double windSpeed = currentWeather.optDouble("windspeed");

                    weatherText = "Погода:\n"
                            + "Температура: " + temperature + " °C\n"
                            + "Скорость ветра: " + windSpeed + " км/ч";
                }

                String finalLatitude = latitude;
                String finalLongitude = longitude;
                String finalWeatherText = weatherText;

                // Обновляем интерфейс в главном потоке
                runOnUiThread(() -> {
                    textViewStatus.setText("Статус: данные получены");

                    textViewIp.setText("IP: " + ip);
                    textViewCity.setText("Город: " + city);
                    textViewRegion.setText("Регион: " + region);
                    textViewCountry.setText("Страна: " + country);
                    textViewCoordinates.setText(
                            "Координаты: " + finalLatitude + ", " + finalLongitude
                    );
                    textViewWeather.setText(finalWeatherText);


                });

            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(() -> {
                    textViewStatus.setText("Статус: ошибка загрузки");
                    Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    // Метод скачивает текст по указанному URL через HttpURLConnection
    private String downloadUrl(String address) throws Exception {
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            // Создаём объект URL
            URL url = new URL(address);

            // Открываем HTTP-соединение
            connection = (HttpURLConnection) url.openConnection();

            // Таймаут чтения
            connection.setReadTimeout(100000);

            // Таймаут подключения
            connection.setConnectTimeout(100000);

            // Метод запроса GET
            connection.setRequestMethod("GET");

            // Разрешаем читать входящие данные
            connection.setDoInput(true);

            // Отключаем кэширование
            connection.setUseCaches(false);

            // Получаем код ответа сервера
            int responseCode = connection.getResponseCode();

            Log.d(TAG, "URL: " + address);
            Log.d(TAG, "Response code: " + responseCode);

            // Если код 200, значит запрос успешный
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();

                // Читаем входной поток в ByteArrayOutputStream
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                int read;

                while ((read = inputStream.read()) != -1) {
                    byteArrayOutputStream.write(read);
                }

                byteArrayOutputStream.close();

                // Возвращаем ответ сервера как строку
                return byteArrayOutputStream.toString();
            } else {
                return "Ошибка сервера. Код: " + responseCode;
            }

        } finally {
            // Закрываем поток
            if (inputStream != null) {
                inputStream.close();
            }

            // Разрываем соединение
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}