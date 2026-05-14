package com.mirea.seminapa.rumireaseminalesson6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Имя XML-файла, где будут храниться настройки
    private static final String PREFS_NAME = "mirea_settings";

    // Ключи, по которым будут сохраняться данные
    private static final String KEY_GROUP = "GROUP";
    private static final String KEY_NUMBER = "NUMBER";
    private static final String KEY_MOVIE = "MOVIE";

    // Поля ввода
    private EditText editTextGroup;
    private EditText editTextNumber;
    private EditText editTextMovie;

    // Текстовый статус на экране
    private TextView textViewStatus;

    // Кнопка сохранения
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextGroup = findViewById(R.id.editTextGroup);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextMovie = findViewById(R.id.editTextMovie);
        textViewStatus = findViewById(R.id.textViewStatus);
        buttonSave = findViewById(R.id.buttonSave);

        // При запуске приложения сразу пробуем загрузить сохранённые данные
        loadData();

        // При нажатии на кнопку вызывается метод сохранения
        buttonSave.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        // Получаем текст из полей ввода
        String group = editTextGroup.getText().toString();
        String numberText = editTextNumber.getText().toString();
        String movie = editTextMovie.getText().toString();

        // Проверяем, что пользователь заполнил все поля
        if (group.isEmpty() || numberText.isEmpty() || movie.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Переводим номер из строки в число
        int number = Integer.parseInt(numberText);

        // Получаем файл настроек mirea_settings.xml
        SharedPreferences sharedPreferences =
                getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Открываем редактор для записи данных
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Сохраняем группу как строку
        editor.putString(KEY_GROUP, group);

        // Сохраняем номер как целое число
        editor.putInt(KEY_NUMBER, number);

        // Сохраняем фильм как строку
        editor.putString(KEY_MOVIE, movie);

        // Применяем изменения
        editor.apply();

        // Обновляем текст на экране
        textViewStatus.setText("Данные сохранены");

        // Показываем короткое сообщение
        Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        // Получаем файл настроек mirea_settings.xml
        SharedPreferences sharedPreferences =
                getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Читаем группу. Если данных нет, вернётся пустая строка
        String group = sharedPreferences.getString(KEY_GROUP, "");

        // Читаем номер. Если данных нет, вернётся 0
        int number = sharedPreferences.getInt(KEY_NUMBER, 0);

        // Читаем фильм. Если данных нет, вернётся пустая строка
        String movie = sharedPreferences.getString(KEY_MOVIE, "");

        // Подставляем сохранённую группу обратно в поле ввода
        editTextGroup.setText(group);

        // Если номер был сохранён, показываем его в поле ввода
        if (number != 0) {
            editTextNumber.setText(String.valueOf(number));
        }

        // Подставляем сохранённый фильм обратно в поле ввода
        editTextMovie.setText(movie);

        // Если хотя бы одно значение было найдено, меняем статус
        if (!group.isEmpty() || number != 0 || !movie.isEmpty()) {
            textViewStatus.setText("Данные загружены из SharedPreferences");
        }
    }
}