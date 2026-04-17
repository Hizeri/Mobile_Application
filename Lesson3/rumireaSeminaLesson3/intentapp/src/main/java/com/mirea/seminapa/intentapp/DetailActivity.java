package com.mirea.seminapa.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Получаем Intent, который запустил эту активность
        Intent intent = getIntent();
        // Извлекаем данные по ключам
        String time = intent.getStringExtra("time_key");
        int square = intent.getIntExtra("square_key", 0); // второй параметр – значение по умолчанию

        // Формируем строку по заданию
        String resultText = "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ "
                + square + ", а текущее время " + time;

        // Отображаем в TextView
        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setText(resultText);
    }
}