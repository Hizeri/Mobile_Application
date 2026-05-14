package com.mirea.seminapa.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Получаем объект FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Если пользователь уже вошёл, сразу открываем главный экран
        if (mAuth.getCurrentUser() != null) {
            openMainActivity();
            return;
        }

        // Находим элементы интерфейса
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        // Кнопка входа
        buttonSignIn.setOnClickListener(v -> signIn());

        // Кнопка создания аккаунта
        buttonCreateAccount.setOnClickListener(v -> createAccount());
    }

    private void signIn() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!validateForm(email, password)) {
            return;
        }

        // Вход через Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show();
                        openMainActivity();
                    } else {
                        Toast.makeText(this,
                                "Ошибка входа: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createAccount() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!validateForm(email, password)) {
            return;
        }

        // Создание аккаунта через Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Аккаунт создан", Toast.LENGTH_SHORT).show();
                        openMainActivity();
                    } else {
                        Toast.makeText(this,
                                "Ошибка регистрации: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateForm(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Введите email");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Введите пароль");
            return false;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Пароль должен быть не меньше 6 символов");
            return false;
        }

        return true;
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}