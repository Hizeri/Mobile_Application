package com.mirea.seminapa.firebaseauth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseAuth";

    private FirebaseAuth mAuth;

    private TextView textViewStatus;
    private TextView textViewDetails;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private Button buttonSignIn;
    private Button buttonCreateAccount;
    private Button buttonVerifyEmail;
    private Button buttonSignOut;

    private LinearLayout layoutEmailPasswordFields;
    private LinearLayout layoutSignedInButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Текстовые поля состояния пользователя
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewDetails = findViewById(R.id.textViewDetails);

        // Поля ввода email и пароля
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Кнопки
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonVerifyEmail = findViewById(R.id.buttonVerifyEmail);
        buttonSignOut = findViewById(R.id.buttonSignOut);

        // Блоки интерфейса
        layoutEmailPasswordFields = findViewById(R.id.layoutEmailPasswordFields);
        layoutSignedInButtons = findViewById(R.id.layoutSignedInButtons);

        // Получаем объект FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Создание аккаунта
        buttonCreateAccount.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            createAccount(email, password);
        });

        // Вход в аккаунт
        buttonSignIn.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            signIn(email, password);
        });

        // Выход из аккаунта
        buttonSignOut.setOnClickListener(v -> signOut());

        // Отправка письма подтверждения
        buttonVerifyEmail.setOnClickListener(v -> sendEmailVerification());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // При запуске проверяем, есть ли уже авторизованный пользователь
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Введите email");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Введите пароль");
            valid = false;
        } else if (password.length() < 6) {
            editTextPassword.setError("Пароль должен быть не меньше 6 символов");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }

    private void createAccount(String email, String password) {
        if (!validateForm(email, password)) {
            return;
        }

        // Создаём пользователя через FirebaseAuth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");

                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Аккаунт создан", Toast.LENGTH_SHORT).show();

                        updateUI(user);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        Toast.makeText(this,
                                "Ошибка создания аккаунта: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();

                        updateUI(null);
                    }
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm(email, password)) {
            return;
        }

        // Вход пользователя через FirebaseAuth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");

                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show();

                        updateUI(user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                        Toast.makeText(this,
                                "Ошибка входа: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();

                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        // Выход из аккаунта
        mAuth.signOut();
        updateUI(null);
        Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        buttonVerifyEmail.setEnabled(false);

        // Отправляем письмо подтверждения email
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> {
                    buttonVerifyEmail.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Письмо отправлено на " + user.getEmail(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this,
                                "Ошибка отправки письма",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Пользователь авторизован
            textViewStatus.setText(
                    "Email: " + user.getEmail() +
                            "\nПодтверждён: " + user.isEmailVerified()
            );

            textViewDetails.setText("Firebase UID: " + user.getUid());

            // Прячем поля входа и показываем кнопки авторизованного пользователя
            layoutEmailPasswordFields.setVisibility(View.GONE);
            layoutSignedInButtons.setVisibility(View.VISIBLE);

            // Если email уже подтверждён, кнопку подтверждения отключаем
            buttonVerifyEmail.setEnabled(!user.isEmailVerified());

        } else {
            // Пользователь не авторизован
            textViewStatus.setText("Статус: пользователь не авторизован");
            textViewDetails.setText("Firebase UID:");

            // Показываем поля входа и прячем кнопки авторизованного пользователя
            layoutEmailPasswordFields.setVisibility(View.VISIBLE);
            layoutSignedInButtons.setVisibility(View.GONE);
        }
    }
}