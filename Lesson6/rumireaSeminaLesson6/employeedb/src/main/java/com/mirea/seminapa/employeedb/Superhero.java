package com.mirea.seminapa.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entity — это класс, по которому Room создаёт таблицу в базе данных
// tableName =  задаёт имя таблицы
@Entity(tableName = "superheroes")
public class Superhero {

    // PrimaryKey — первичный ключ таблицы
    // autoGenerate = true означает, что id будет создаваться автоматически
    @PrimaryKey(autoGenerate = true)
    public long id;

    // Столбец name — имя супергероя
    public String name;

    // Столбец power — суперспособность
    public String power;

    // Столбец universe — вселенная героя
    public String universe;
}