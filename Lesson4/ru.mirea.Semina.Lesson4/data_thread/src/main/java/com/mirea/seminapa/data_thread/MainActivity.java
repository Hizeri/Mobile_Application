package com.mirea.seminapa.data_thread; // ваш пакет

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.mirea.seminapa.data_thread.databinding.ActivityMainBinding;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView tvInfo = binding.textViewLog;
        tvInfo.setText(""); // очищаем

        // 1. Runnable для runOnUiThread
        final Runnable runn1 = new Runnable() {
            @Override
            public void run() {
                // Добавляем сообщение в TextView (можно и setText, но append покажет историю)
                tvInfo.append("1. runOnUiThread (runn1) – выполнен немедленно в UI-потоке\n");
            }
        };

        // 2. Runnable для View.post
        final Runnable runn2 = new Runnable() {
            @Override
            public void run() {
                tvInfo.append("2. View.post (runn2) – добавлен в очередь UI-потока, выполнен после текущих задач\n");
            }
        };

        // 3. Runnable для View.postDelayed
        final Runnable runn3 = new Runnable() {
            @Override
            public void run() {
                tvInfo.append("3. View.postDelayed (runn3) – выполнен с задержкой 2000 мс после вызова\n");
                tvInfo.append("\n ВЫВОД \n");
                tvInfo.append("runOnUiThread: отправляет задачу в главный поток немедленно (но после завершения текущего блока).\n");
                tvInfo.append("View.post: аналогично, но гарантирует, что View уже привязан и измерен.\n");
                tvInfo.append("View.postDelayed: откладывает выполнение на указанное время.\n");
                tvInfo.append("Последовательность выполнения: runOnUiThread → post → postDelayed (по истечении задержки).\n");
            }
        };

        // Запускаем фоновый поток, который вызывает эти Runnable с паузами
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);        // имитация долгой операции
                    runOnUiThread(runn1);              // вызываем runOnUiThread
                    TimeUnit.SECONDS.sleep(1);        // пауза 1 секунда
                    tvInfo.postDelayed(runn3, 2000);  // отложенный вызов (через 2 секунды)
                    tvInfo.post(runn2);                // немедленный post
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}