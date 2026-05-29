package com.mirea.seminapa.mireaproject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWorkFragment extends Fragment {

    // TextView для вывода списка файлов и их содержимого
    private TextView textViewFiles;

    // Кнопка обновления списка файлов
    private Button buttonRefreshFiles;

    //  кнопка добавления нового файла
    private FloatingActionButton fabAddFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_work, container, false);

        // Находим элементы интерфейса внутри view фрагмента
        textViewFiles = view.findViewById(R.id.textViewFiles);
        buttonRefreshFiles = view.findViewById(R.id.buttonRefreshFiles);
        fabAddFile = view.findViewById(R.id.fabAddFile);

        // При открытии фрагмента сразу показываем список сохранённых файлов
        showFileList();

        // При нажатии на кнопку обновляем список файлов
        buttonRefreshFiles.setOnClickListener(v -> showFileList());

        // При нажатии на FAB открываем окно создания нового файла
        fabAddFile.setOnClickListener(v -> showCreateFileDialog());

        // Возвращаем готовый экран фрагмента
        return view;
    }

    private void showCreateFileDialog() {
        // Создаём отдельную разметку для диалогового окна
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_file_note, null);

        // Находим поля ввода внутри диалога
        EditText editTextFileName = dialogView.findViewById(R.id.editTextDialogFileName);
        EditText editTextText = dialogView.findViewById(R.id.editTextDialogText);

        // Подставляем пример имени файла для удобства
        editTextFileName.setText("cat_note.txt");

        // Создаём всплывающее окно
        new AlertDialog.Builder(requireContext())
                .setTitle("Создание записи") // заголовок диалога
                .setView(dialogView) // вставляем свою XML-разметку в диалог
                .setPositiveButton("Сохранить", (dialog, which) -> {

                    // Получаем имя файла и текст из полей
                    String fileName = editTextFileName.getText().toString();
                    String text = editTextText.getText().toString();

                    // Если пользователь не написал .txt, добавляем расширение автоматически
                    if (!fileName.endsWith(".txt")) {
                        fileName = fileName + ".txt";
                    }

                    // Проверяем, что имя файла и текст не пустые
                    if (fileName.isEmpty() || text.isEmpty()) {
                        Toast.makeText(getContext(), "Заполните имя файла и текст", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Сохраняем файл во внутреннее хранилище
                    saveFile(fileName, text);

                    // После сохранения обновляем список файлов на экране
                    showFileList();
                })
                .setNegativeButton("Отмена", null) // кнопка закрытия без сохранения
                .show(); // показываем диалог
    }

    private void saveFile(String fileName, String text) {
        // Поток записи пока пустой
        FileOutputStream outputStream = null;

        try {
            // Открываем файл во внутреннем хранилище приложения
            // MODE_PRIVATE означает, что файл доступен только этому приложению
            // Если файл с таким именем уже есть, он будет перезаписан
            outputStream = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE);

            // Записываем текст в файл как набор байтов
            outputStream.write(text.getBytes());

            // Сообщаем пользователю, что файл сохранён
            Toast.makeText(getContext(), "Файл сохранён: " + fileName, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            // Если произошла ошибка записи, выводим её в Logcat
            e.printStackTrace();

            // Сообщаем пользователю об ошибке
            Toast.makeText(getContext(), "Ошибка сохранения файла", Toast.LENGTH_SHORT).show();

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

    private String readFile(String fileName) {
        // Поток чтения пока пустой
        FileInputStream inputStream = null;

        try {
            // Открываем файл из внутреннего хранилища для чтения
            inputStream = requireContext().openFileInput(fileName);

            // Создаём массив байтов размером с файл
            byte[] bytes = new byte[inputStream.available()];

            // Читаем содержимое файла в массив bytes
            inputStream.read(bytes);

            // Преобразуем байты обратно в строку и возвращаем текст
            return new String(bytes);

        } catch (IOException e) {
            // Если файл не удалось прочитать, возвращаем текст ошибки
            return "Ошибка чтения файла";

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

    private void showFileList() {
        // Получаем список всех файлов из внутреннего хранилища приложения
        String[] files = requireContext().fileList();

        // StringBuilder нужен, чтобы собрать общий текст для вывода
        StringBuilder builder = new StringBuilder();

        // Проходим по всем найденным файлам
        for (String fileName : files) {

            // Показываем только текстовые файлы .txt
            // Это нужно, чтобы не пытаться читать аудио или служебные файлы как текст
            if (!fileName.endsWith(".txt")) {
                continue;
            }

            // Добавляем имя файла в общий текст
            builder.append("Файл: ").append(fileName).append("\n");

            // Читаем содержимое файла и добавляем его в общий текст
            builder.append("Текст: ").append(readFile(fileName)).append("\n");

            // Разделитель между файлами
            builder.append("-------------------------\n");
        }

        // Если текстовых файлов не найдено
        if (builder.length() == 0) {
            textViewFiles.setText("Текстовые файлы ещё не созданы");
            return;
        }

        // Выводим список файлов и их содержимое на экран
        textViewFiles.setText(builder.toString());
    }
}