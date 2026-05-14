package com.mirea.seminapa.securesharedpreferences;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    // Название файла, где будут храниться зашифрованные данные
    private static final String SECURE_PREFS_NAME = "secret_shared_prefs";

    // Ключ, по которому будет сохраняться имя поэта
    private static final String KEY_POET = "poet";

    // Поле ввода имени поэта
    private EditText editTextPoet;

    private Button buttonSave;

    // Текстовое поле для вывода результата
    private TextView textViewResult;

    // Объект защищённых SharedPreferences
    private SharedPreferences secureSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPoet = findViewById(R.id.editTextPoet);
        buttonSave = findViewById(R.id.buttonSave);
        textViewResult = findViewById(R.id.textViewResult);

        try {
            // Создаём главный ключ для шифрования данных
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Создаём защищённые EncryptedSharedPreferences
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    this, // контекст Activity
                    SECURE_PREFS_NAME, // имя файла с настройками
                    masterKey, // главный ключ шифрования
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // шифрование ключей
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // шифрование значений
            );

            // При запуске приложения пробуем загрузить ранее сохранённое значение
            loadPoet();

        } catch (GeneralSecurityException | IOException e) {
            // Если защищённое хранилище создать не получилось — выбрасываем ошибку
            throw new RuntimeException(e);
        }

        // При нажатии на кнопку вызывается метод сохранения
        buttonSave.setOnClickListener(v -> savePoet());
    }

    private void savePoet() {
        // Получаем текст из поля ввода
        String poet = editTextPoet.getText().toString();

        // Проверяем, что пользователь что-то ввёл
        if (poet.isEmpty()) {
            Toast.makeText(this, "Введите имя поэта", Toast.LENGTH_SHORT).show();
            return;
        }

        // Сохраняем имя поэта в зашифрованные настройки
        secureSharedPreferences.edit() // открывает режим редактирования
                .putString(KEY_POET, poet) // сохраняет строку по ключу KEY_POET
                .apply(); // применяем изменения

        // Показываем результат на экране
        textViewResult.setText("Сохранено: " + poet);

        // Показываем короткое всплывающее сообщение
        Toast.makeText(this, "Данные сохранены защищённо", Toast.LENGTH_SHORT).show();
    }

    private void loadPoet() {
        // Читаем имя поэта из зашифрованных настроек
        // Если значения нет, вернётся пустая строка
        String poet = secureSharedPreferences.getString(KEY_POET, "");

        // Если значение найдено, показываем его в интерфейсе
        if (!poet.isEmpty()) {
            editTextPoet.setText(poet);
            textViewResult.setText("Загружено из защищённой памяти: " + poet);
        }
    }
}