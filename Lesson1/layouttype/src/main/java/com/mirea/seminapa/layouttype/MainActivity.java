package com.mirea.seminapa.layouttype;

import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.CheckBox;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Получаем кнопку и меняем текст
        Button button = findViewById(R.id.button);
        button.setText("MireaButton");

// Получаем чекбокс и ставим галочку
        CheckBox checkBox = findViewById(R.id.check_box);
        checkBox.setChecked(true);
        TextView tv = findViewById(R.id.textViewStudent);
        tv.setText("New text in MIREA");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}