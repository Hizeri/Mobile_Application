package com.mirea.seminapa.employeedb;

import android.app.Application;

import androidx.room.Room;

// Application создаётся один раз при запуске приложения
public class App extends Application {

    // Статический экземпляр приложения
    private static App instance;

    // Экземпляр базы данных
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        // Сохраняем ссылку на приложение
        instance = this;

        // Создаём базу данных Room
        database = Room.databaseBuilder(
                        this, // контекст приложения
                        AppDatabase.class, // класс базы данных
                        "superhero_database" // имя файла базы данных
                )
                //разрешает делать запросы к базе прямо в главном потоке приложения
                .allowMainThreadQueries()
                .build();
    }

    // Метод для получения экземпляра приложения
    public static App getInstance() {
        return instance;
    }

    // Метод для получения базы данных
    public AppDatabase getDatabase() {
        return database;
    }
}