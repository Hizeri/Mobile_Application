package com.mirea.seminapa.rumireaseminalesson4; // ваш пакет

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mirea.seminapa.rumireaseminalesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Создаём объект привязки
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Обработчик для кнопки Play
        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Изменяем текст статуса
                binding.statusTextView.setText("Статус: воспроизведение");
                // Можно также вывести Toast для наглядности
                Toast.makeText(MainActivity.this, "Play нажата", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик для кнопки Pause
        binding.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statusTextView.setText("Статус: пауза");
                Toast.makeText(MainActivity.this, "Pause нажата", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик для кнопки Stop
        binding.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statusTextView.setText("Статус: остановлено");
                Toast.makeText(MainActivity.this, "Stop нажата", Toast.LENGTH_SHORT).show();
            }
        });
    }
}