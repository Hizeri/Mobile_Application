package com.mirea.seminapa.employeedb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// Database — главный класс базы данных Room
// entities указывает, какие таблицы будут в базе
// version — версия базы данных
@Database(entities = {Superhero.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // Метод возвращает DAO для работы с таблицей супергероев
    public abstract SuperheroDao superheroDao();
}