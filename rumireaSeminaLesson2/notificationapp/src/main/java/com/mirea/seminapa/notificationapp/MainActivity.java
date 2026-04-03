package com.mirea.seminapa.notificationapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "notification_channel"; // Уникальный идентификатор канала уведомлений
    private static final int NOTIFICATION_ID = 1; // Уникальный идентификатор уведомления
    private static final int PERMISSION_CODE = 100; //код запроса разрешения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Запрос разрешения для Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_CODE);
            }
        }

        createNotificationChannel();
    }

    // Метод для создания канала уведомлений
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Канал уведомлений", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class); // Получаем системный сервис уведомлений
            if (manager != null) {   // Регистрируем канал в системе
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    //Обработка ответа пользователя на запрос разрешения
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Разрешение получено", Toast.LENGTH_SHORT).show();
        }
    }

    // Обработчик нажатия кнопки
    public void sendNotification(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID); // Строитель уведомления
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
        builder.setContentTitle("МИРЭА");
        builder.setContentText("Семина Полина Алексеевна, РТУ МИРЭА");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(false);

        // Получаем системный сервис управления уведомлениями
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) { // Отправляем уведомление, если сервис получен
            manager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}