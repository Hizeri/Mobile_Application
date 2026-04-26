package com.mirea.seminapa.thread; // замените на свой пакет

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mirea.seminapa.thread.databinding.ActivityMainBinding;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int counter = 0; // счётчик потоков

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ===== 1. Работа с главным потоком =====
        Thread mainThread = Thread.currentThread();
        // Выводим информацию в TextView
        binding.textViewThreadInfo.setText("Имя текущего потока: " + mainThread.getName());
        // Меняем имя
        mainThread.setName("ГРУППА: БСБО-09-23, №22, ФИЛЬМ: Интерстеллар");
        binding.textViewThreadInfo.append("\nНовое имя потока: " + mainThread.getName());
        // Выводим стек и приоритет в лог
        Log.d("MainActivity", "Stack: " + Arrays.toString(mainThread.getStackTrace()));
        Log.d("MainActivity", "Приоритет: " + mainThread.getPriority());
        Log.d("MainActivity", "Группа: " + mainThread.getThreadGroup());

        // ===== 2. Кнопка для расчёта среднего (фоновый поток) =====
        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoursStr = binding.editTextHours.getText().toString();
                String daysStr = binding.editTextDays.getText().toString();
                if (hoursStr.isEmpty() || daysStr.isEmpty()) {
                    binding.textViewResult.setText("Введите оба значения");
                    return;
                }
                int totalHours = Integer.parseInt(hoursStr);
                int totalDays = Integer.parseInt(daysStr);
                if (totalDays == 0) {
                    binding.textViewResult.setText("Количество дней не может быть 0");
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final float average = (float) totalHours / totalDays;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.textViewResult.setText("Среднее пар в день: " + average);
                                Toast.makeText(MainActivity.this, "Расчёт завершён", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });

        // ===== 3. Кнопка для демонстрации долгой операции в фоновом потоке =====
        binding.buttonBlocking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", "Запущен поток № " + numberThread +
                                " студентом группы БСБО-09-23, номер по списку 22");
                        long endTime = System.currentTimeMillis() + 20 * 1000; // 20 секунд
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Log.d("ThreadProject", "Выполнен поток № " + numberThread);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,
                                        "Долгая операция завершена в потоке №" + numberThread,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
    }
}