package com.mirea.seminapa.favoritebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvBookResult;

    // Регистрируем лаунчер для получения результата от ShareActivity
    private final ActivityResultLauncher<Intent> bookLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), // контракт: запускаем активность и ждём результат
            result -> {
                // Проверяем, что всё прошло успешно  и что данные не пустые
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String userBook = data.getStringExtra("user_book_key");
                    tvBookResult.setText("Название Вашей любимой книги: " + userBook);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBookResult = findViewById(R.id.tvBookResult);
        Button btnOpen = findViewById(R.id.btnOpenInput);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                bookLauncher.launch(intent); // Запускаем вторую активность через лаунчер, чтобы получить результат
            }
        });
    }
}