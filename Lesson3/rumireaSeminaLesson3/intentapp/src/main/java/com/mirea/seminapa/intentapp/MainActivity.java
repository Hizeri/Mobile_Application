package com.mirea.seminapa.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.mirea.seminapa.intentapp.DetailActivity;
import com.mirea.seminapa.intentapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Получаем текущее время в миллисекундах
                long dateInMillis = System.currentTimeMillis();
                //  Форматируем в читаемый вид
                String format = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                String dateString = sdf.format(new Date(dateInMillis));

                //  Задаём свой номер
                int myNumber = 22;   //
                int square = myNumber * myNumber;

                // Создаём явный Intent для перехода к DetailActivity
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                //  Кладём данные (время и квадрат) в Intent
                intent.putExtra("time_key", dateString);
                intent.putExtra("square_key", square);
                //  Запускаем вторую активность
                startActivity(intent);
            }
        });
    }
}