package com.mirea.seminapa.multiactivity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        String receivedData = getIntent().getStringExtra("USER_DATA"); // получаем данные по ключу

        // Находим TextView и выводим данные
        TextView textView = findViewById(R.id.textViewDisplay);
        if (receivedData != null && !receivedData.isEmpty()) {
            textView.setText("Получено: " + receivedData);
        }
        else {
            textView.setText("Ничего не получено");
        }
    }
}