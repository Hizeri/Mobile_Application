package com.mirea.seminapa.toastapp;  // ваш пакет

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showToast(View view) {
        EditText editText = findViewById(R.id.editTextInput);
        String text = editText.getText().toString();
        int length = text.length();
        String message = "СТУДЕНТ №22 ГРУППА БСБО-09-23 Количество символов - " + length;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}