package com.mirea.seminapa.notebook;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {


    // На устройстве Documents/MireaNotebook/
    private static final String NOTEBOOK_FOLDER = Environment.DIRECTORY_DOCUMENTS + "/MireaNotebook/";

    // Поле для имени файла
    private EditText editTextFileName;

    // Поле для текста цитаты
    private EditText editTextQuote;

    // Текстовый статус на экране
    private TextView textViewStatus;

    // Кнопка сохранения файла
    private Button buttonSave;

    // Кнопка загрузки файла
    private Button buttonLoad;

    // Кнопка создания готовых примеров
    private Button buttonCreateExamples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFileName = findViewById(R.id.editTextFileName);
        editTextQuote = findViewById(R.id.editTextQuote);
        textViewStatus = findViewById(R.id.textViewStatus);
        buttonSave = findViewById(R.id.buttonSave);
        buttonLoad = findViewById(R.id.buttonLoad);
        buttonCreateExamples = findViewById(R.id.buttonCreateExamples);

        // Подставляем пример имени файла, чтобы не вводить его каждый раз
        editTextFileName.setText("quote_pushkin.txt");

        // Обработчик кнопки сохранения файла
        buttonSave.setOnClickListener(v -> {
            // Получаем имя файла и текст цитаты из полей ввода
            String fileName = editTextFileName.getText().toString();
            String quote = editTextQuote.getText().toString();

            // Проверяем, что оба поля заполнены
            if (fileName.isEmpty() || quote.isEmpty()) {
                Toast.makeText(this, "Введите имя файла и цитату", Toast.LENGTH_SHORT).show();
                return;
            }

            // Сохраняем текст в файл
            saveTextFile(fileName, quote);
        });

        // Обработчик кнопки загрузки файла
        buttonLoad.setOnClickListener(v -> {
            // Получаем имя файла из поля ввода
            String fileName = editTextFileName.getText().toString();

            // Проверяем, что имя файла введено
            if (fileName.isEmpty()) {
                Toast.makeText(this, "Введите имя файла", Toast.LENGTH_SHORT).show();
                return;
            }

            // Загружаем текст из файла
            loadTextFile(fileName);
        });

        // Обработчик кнопки создания примеров
        buttonCreateExamples.setOnClickListener(v -> createExampleFiles());
    }

    // Метод сохраняет текстовый файл в папку Documents/MireaNotebook/
    private void saveTextFile(String fileName, String text) {
        try {
            // Если файл уже существует, удаляем его для перезаписи
            deleteFileIfExists(fileName);

            // ContentValues описывает файл, который мы хотим создать
            ContentValues values = new ContentValues();

            // Указываем имя файла
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);

            // Указываем тип файла
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");

            // Указываем папку сохранения
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, NOTEBOOK_FOLDER);

            // Получаем ContentResolver для работы с MediaStore
            ContentResolver resolver = getContentResolver();

            // Указываем, что работаем с внешним хранилищем устройства
            Uri collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

            // Создаём файл через MediaStore
            Uri fileUri = resolver.insert(collection, values);

            // Если Uri не создался, значит файл создать не удалось
            if (fileUri == null) {
                Toast.makeText(this, "Не удалось создать файл", Toast.LENGTH_SHORT).show();
                return;
            }

            // Открываем поток записи в созданный файл
            OutputStream outputStream = resolver.openOutputStream(fileUri);

            if (outputStream != null) {
                // Записываем текст в файл в кодировке UTF-8
                outputStream.write(text.getBytes(StandardCharsets.UTF_8));

                // Закрываем поток после записи
                outputStream.close();

                // Показываем результат на экране
                textViewStatus.setText("Файл сохранён: Documents/MireaNotebook/" + fileName);

                // Показываем короткое сообщение
                Toast.makeText(this, "Файл сохранён", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            // Если произошла ошибка записи, выводим её в Logcat
            e.printStackTrace();

            // Сообщаем пользователю об ошибке
            Toast.makeText(this, "Ошибка сохранения файла", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод загружает текст из файла и вставляет его в поле цитаты
    private void loadTextFile(String fileName) {
        try {
            // Ищем Uri файла по его имени
            Uri fileUri = findFileUri(fileName);

            // Если файл не найден, останавливаем метод
            if (fileUri == null) {
                Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
                return;
            }

            // Открываем поток чтения из найденного файла
            InputStream inputStream = getContentResolver().openInputStream(fileUri);

            // Если поток не открылся, файл прочитать нельзя
            if (inputStream == null) {
                Toast.makeText(this, "Не удалось открыть файл", Toast.LENGTH_SHORT).show();
                return;
            }

            // BufferedReader нужен для удобного чтения текста построчно
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );

            // StringBuilder собирает весь текст файла в одну строку
            StringBuilder builder = new StringBuilder();

            String line;

            // Читаем файл построчно, пока строки не закончатся
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            // Закрываем reader и inputStream
            reader.close();
            inputStream.close();

            // Вставляем прочитанный текст обратно в поле цитаты
            editTextQuote.setText(builder.toString().trim());

            // Обновляем статус
            textViewStatus.setText("Файл загружен: Documents/MireaNotebook/" + fileName);

            // Показываем сообщение
            Toast.makeText(this, "Файл загружен", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            // Если произошла ошибка чтения, выводим её в Logcat
            e.printStackTrace();

            // Сообщаем пользователю об ошибке
            Toast.makeText(this, "Ошибка чтения файла", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод создаёт два готовых файла с цитатами известных людей
    private void createExampleFiles() {
        // Создаём файл с цитатой Пушкина
        saveTextFile(
                "quote_pushkin.txt",
                "А. С. Пушкин: «Чем меньше женщину мы любим, тем легче нравимся мы ей и тем её вернее губим средь обольстительных сетей»"
        );

        // Создаём файл с цитатой Гагарина
        saveTextFile(
                "quote_gagarin.txt",
                "Ю. А. Гагарин: Поехали!"
        );

        // Показываем общий статус
        textViewStatus.setText("Созданы файлы: quote_pushkin.txt и quote_gagarin.txt");
    }

    // Метод ищет файл в Documents/MireaNotebook/ по имени
    private Uri findFileUri(String fileName) {
        // Получаем ContentResolver
        ContentResolver resolver = getContentResolver();

        // Указываем, где искать файл
        Uri collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

        // Указываем, какие поля хотим получить из MediaStore
        String[] projection = new String[]{
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        // Условие поиска: имя файла и путь к папке
        String selection = MediaStore.MediaColumns.DISPLAY_NAME + "=? AND "
                + MediaStore.MediaColumns.RELATIVE_PATH + "=?";

        // Значения для условия поиска
        String[] selectionArgs = new String[]{
                fileName,
                NOTEBOOK_FOLDER
        };

        // Выполняем запрос к MediaStore
        Cursor cursor = resolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null) {
            try {
                // Если найден хотя бы один файл
                if (cursor.moveToFirst()) {
                    // Получаем номер столбца с ID файла
                    int idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);

                    // Получаем ID найденного файла
                    long id = cursor.getLong(idColumn);

                    // Собираем Uri файла по его ID
                    return Uri.withAppendedPath(collection, String.valueOf(id));
                }
            } finally {
                // Cursor обязательно нужно закрывать
                cursor.close();
            }
        }

        // Если файл не найден, возвращаем null
        return null;
    }

    // Метод удаляет файл, если он уже существует
    private void deleteFileIfExists(String fileName) {
        // Ищем файл по имени
        Uri fileUri = findFileUri(fileName);

        // Если файл найден, удаляем его
        if (fileUri != null) {
            getContentResolver().delete(fileUri, null, null);
        }
    }
}