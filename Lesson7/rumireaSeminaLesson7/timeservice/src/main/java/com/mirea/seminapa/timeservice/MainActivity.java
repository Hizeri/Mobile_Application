package com.mirea.seminapa.timeservice;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TimeService";

    // Адрес сервера времени
    private final String host = "time.nist.gov";

    // Порт сервера времени
    private final int port = 13;

    private TextView textViewRaw;
    private TextView textViewDate;
    private TextView textViewTime;
    private Button buttonGetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим элементы интерфейса
        textViewRaw = findViewById(R.id.textViewRaw);
        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);
        buttonGetTime = findViewById(R.id.buttonGetTime);

        // По нажатию кнопки получаем время с сервера
        buttonGetTime.setOnClickListener(v -> getTimeFromServer());
    }

    private void getTimeFromServer() {
        // Сразу показываем, что началась загрузка
        textViewRaw.setText("Подключаемся к серверу...");
        textViewDate.setText("Дата:");
        textViewTime.setText("Время:");

        // Сетевой запрос нельзя делать в главном потоке,
        // поэтому запускаем отдельный поток
        new Thread(() -> {
            String result;

            try {
                // Создаём соединение с сервером времени
                Socket socket = new Socket(host, port);

                // Получаем reader для чтения ответа сервера
                BufferedReader reader = SocketUtils.getReader(socket);

                // Первая строка обычно служебная, пропускаем её
                reader.readLine();

                // Вторая строка содержит дату и время
                result = reader.readLine();

                // Закрываем соединение
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
                result = "Ошибка подключения: " + e.getMessage();
            }

            String finalResult = result;

            // Возвращаемся в главный поток, чтобы обновить TextView
            runOnUiThread(() -> showResult(finalResult));

        }).start();
    }

    private void showResult(String result) {
        // Показываем исходный ответ сервера
        textViewRaw.setText("Ответ сервера:\n" + result);

        Log.d(TAG, "Ответ сервера: " + result);

        // Проверяем, что ответ не пустой и не ошибка
        if (result == null || result.isEmpty() || result.startsWith("Ошибка")) {
            Toast.makeText(this, "Не удалось получить время", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Разбиваем строку сервера по пробелам
            String[] parts = result.trim().split("\\s+");

            // У сервера time.nist.gov обычно:
            // parts[1] — дата
            // parts[2] — время UTC
            String date = parts[1];
            String utcTime = parts[2];

            // Прибавляем 3 часа, чтобы получить местное время UTC+3
            String localTime = addThreeHours(utcTime);

            // Выводим дату
            textViewDate.setText("Дата: " + date);

            // Выводим UTC-время и местное время
            textViewTime.setText(
                    "Время UTC: " + utcTime +
                            "\nМестное время UTC+3: " + localTime
            );

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка разбора строки", Toast.LENGTH_SHORT).show();
        }
    }

    private String addThreeHours(String utcTime) {
        // utcTime приходит в формате HH:mm:ss, например 15:03:25
        String[] timeParts = utcTime.split(":");

        int hours = Integer.parseInt(timeParts[0]);
        int minutes = Integer.parseInt(timeParts[1]);
        int seconds = Integer.parseInt(timeParts[2]);

        // Прибавляем 3 часа
        hours = hours + 3;

        // Если вышли за 24 часа, начинаем новый день
        if (hours >= 24) {
            hours = hours - 24;
        }

        // Возвращаем время в красивом формате HH:mm:ss
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}