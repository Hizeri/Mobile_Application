package com.mirea.seminapa.employeedb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Поле ввода имени героя
    private EditText editTextName;

    // Поле ввода силы героя
    private EditText editTextPower;

    // Поле ввода вселенной героя
    private EditText editTextUniverse;

    // Кнопка сохранения героя
    private Button buttonSaveHero;

    // Кнопка вывода всех героев
    private Button buttonShowHeroes;

    // TextView для вывода списка героев
    private TextView textViewHeroes;

    // DAO — объект для выполнения запросов к таблице superheroes
    private SuperheroDao superheroDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Находим поля ввода по id
        editTextName = findViewById(R.id.editTextName);
        editTextPower = findViewById(R.id.editTextPower);
        editTextUniverse = findViewById(R.id.editTextUniverse);

        // Находим кнопки
        buttonSaveHero = findViewById(R.id.buttonSaveHero);
        buttonShowHeroes = findViewById(R.id.buttonShowHeroes);

        // Находим TextView для вывода данных
        textViewHeroes = findViewById(R.id.textViewHeroes);

        // Получаем объект базы данных из класса App
        AppDatabase database = App.getInstance().getDatabase();

        // Получаем DAO для работы с таблицей superheroes
        superheroDao = database.superheroDao();

        // Подставляем пример данных для удобного тестирования
        editTextName.setText("Мама");
        editTextPower.setText("Любовь");
        editTextUniverse.setText("Реальная");

        // При нажатии на кнопку сохраняем героя в базу
        buttonSaveHero.setOnClickListener(v -> saveHero());

        // При нажатии на кнопку показываем всех героев из базы
        buttonShowHeroes.setOnClickListener(v -> showHeroes());
    }

    // Метод сохраняет героя в базу данных
    private void saveHero() {
        // Получаем данные из полей ввода
        String name = editTextName.getText().toString();
        String power = editTextPower.getText().toString();
        String universe = editTextUniverse.getText().toString();

        // Проверяем, что все поля заполнены
        if (name.isEmpty() || power.isEmpty() || universe.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создаём объект героя
        Superhero superhero = new Superhero();

        // Заполняем поля объекта
        superhero.name = name;
        superhero.power = power;
        superhero.universe = universe;

        // Добавляем героя в базу данных через DAO
        superheroDao.insert(superhero);

        // Показываем сообщение об успешном сохранении
        Toast.makeText(this, "Герой сохранён в базу данных", Toast.LENGTH_SHORT).show();

        // Очищаем поля после сохранения
        editTextName.setText("");
        editTextPower.setText("");
        editTextUniverse.setText("");
    }

    // Метод показывает всех героев из базы данных
    private void showHeroes() {
        // Получаем список всех героев из таблицы superheroes
        List<Superhero> heroes = superheroDao.getAll();

        // Если список пустой, выводим сообщение
        if (heroes.isEmpty()) {
            textViewHeroes.setText("В базе пока нет героев");
            return;
        }

        // StringBuilder нужен для сборки красивого текста
        StringBuilder builder = new StringBuilder();

        // Проходим по каждому герою из списка
        for (Superhero hero : heroes) {
            builder.append("ID: ").append(hero.id).append("\n");
            builder.append("Имя: ").append(hero.name).append("\n");
            builder.append("Сила: ").append(hero.power).append("\n");
            builder.append("Вселенная: ").append(hero.universe).append("\n");
            builder.append("-------------------------\n");
        }

        // Показываем список героев на экране
        textViewHeroes.setText(builder.toString());
    }
}