package com.mirea.seminapa.serviceapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class PlayerService extends Service {

    private MediaPlayer mediaPlayer;
    private static final String CHANNEL_ID = "MusicPlayerChannel";
    private static final int NOTIFICATION_ID = 1;
    // СВОЁ НАЗВАНИЕ КОМПОЗИЦИИ (обязательно)
    private static final String TRACK_NAME = "Midnight Ride";

    @Override
    public void onCreate() {
        super.onCreate();
        // Загружаем аудиофайл из res/raw/music.mp3
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            // Обработчик окончания трека
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Останавливаем сервис и убираем уведомление
                    stopForeground(true);
                    stopSelf();
                    Log.d("PlayerService", "Воспроизведение завершено");
                }
            });
        } else {
            Log.e("PlayerService", "Не удалось загрузить аудиофайл");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            // Показываем уведомление с названием трека
            showNotification(TRACK_NAME);
            Log.d("PlayerService", "Воспроизведение начато: " + TRACK_NAME);
        }
        // Если сервис убьют – попробовать перезапустить
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // не привязываемся
    }

    private void showNotification(String trackName) {
        // Создаём канал для Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Музыкальный плеер",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        // Намерение при клике на уведомление
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Уведомление с названием композиции
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Музыкальный плеер")
                .setContentText("Сейчас играет: " + trackName)   // <-- здесь название
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .build();

        // Запуск foreground сервиса
        startForeground(NOTIFICATION_ID, notification);
    }
}