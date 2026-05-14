package com.mirea.seminapa.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

// DAO — это набор команд для базы данных
@Dao
public interface SuperheroDao {

    // Получить всех героев из таблицы superheroes
    @Query("SELECT * FROM superheroes")
    List<Superhero> getAll();

    // Получить одного героя по id
    @Query("SELECT * FROM superheroes WHERE id = :id")
    Superhero getById(long id);

    // Добавить героя в базу данных
    @Insert
    void insert(Superhero superhero);

    // Обновить данные героя
    @Update
    void update(Superhero superhero);

    // Удалить героя из базы данных
    @Delete
    void delete(Superhero superhero);

    // Удалить всех героев из таблицы superheroes
    @Query("DELETE FROM superheroes")
    void deleteAll();
}