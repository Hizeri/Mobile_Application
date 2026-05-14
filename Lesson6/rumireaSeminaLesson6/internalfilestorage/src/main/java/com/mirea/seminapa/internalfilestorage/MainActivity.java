package com.mirea.seminapa.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Имя файла во внутреннем хранилище приложения
    // Файл будет храниться внутри памяти приложения, не в Documents
    private static final String FILE_NAME = "history_date.txt";

    // Поле для памятной даты
    private EditText editTextDate;

    // Поле для описания события
    private EditText editTextDescription;

    // TextView для вывода содержимого файла
    private TextView textViewFileContent;

    // Кнопка сохранения файла
    private Button buttonSaveFile;

    // Кнопка чтения файла
    private Button buttonReadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDate = findViewById(R.id.editTextDate);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewFileContent = findViewById(R.id.textViewFileContent);
        buttonSaveFile = findViewById(R.id.buttonSaveFile);
        buttonReadFile = findViewById(R.id.buttonReadFile);

        // Подставляем пример даты, чтобы было удобно тестировать
        editTextDate.setText("12 апреля 1961 года");

        // Подставляем пример описания события
        editTextDescription.setText("Юрий Гагарин совершил первый в мире полёт человека в космос на корабле «Восток-1».");

        // При нажатии на кнопку сохраняем текст в файл
        buttonSaveFile.setOnClickListener(v -> saveToFile());

        // При нажатии на кнопку читаем текст из файла
        buttonReadFile.setOnClickListener(v -> readFromFile());
    }

    private void saveToFile() {
        // Получаем дату из поля ввода
        String date = editTextDate.getText().toString();

        // Получаем описание из поля ввода
        String description = editTextDescription.getText().toString();

        // Проверяем, что оба поля заполнены
        if (date.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Заполните дату и описание", Toast.LENGTH_SHORT).show();
            return;
        }

        // Формируем общий текст, который будет записан в файл
        String textToSave = "Дата: " + date + "\nОписание: " + description;

        // Поток записи пока пустой
        FileOutputStream outputStream = null;

        try {
            // Открываем файл для записи во внутреннее хранилище приложения
            // MODE_PRIVATE означает: файл доступен только этому приложению
            // Если файл уже есть, он будет перезаписан
            outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            // Записываем строку в файл как набор байтов
            outputStream.write(textToSave.getBytes());

            // Показываем сообщение об успешном сохранении
            Toast.makeText(this, "Файл сохранён", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            // Если произошла ошибка записи, выводим её в Logcat
            e.printStackTrace();

            // Сообщаем пользователю об ошибке
            Toast.makeText(this, "Ошибка записи файла", Toast.LENGTH_SHORT).show();

        } finally {
            try {
                // Закрываем поток записи, если он был открыт
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readFromFile() {
        // Поток чтения пока пустой
        FileInputStream inputStream = null;

        try {
            // Открываем файл для чтения из внутреннего хранилища
            inputStream = openFileInput(FILE_NAME);

            // Создаём массив байтов размером с файл
            byte[] bytes = new byte[inputStream.available()];

            // Читаем содержимое файла в массив bytes
            inputStream.read(bytes);

            // Преобразуем байты обратно в строку
            String fileText = new String(bytes);

            // Показываем содержимое файла на экране
            textViewFileContent.setText(fileText);

        } catch (IOException e) {
            // Если файла ещё нет или его не удалось прочитать
            e.printStackTrace();

            // Сообщаем пользователю, что файл ещё не создан
            Toast.makeText(this, "Файл ещё не создан", Toast.LENGTH_SHORT).show();

        } finally {
            try {
                // Закрываем поток чтения, если он был открыт
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}