package com.mirea.seminapa.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mirea.seminapa.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Создаём Handler для главного потока (он будет получать результат из MyLooper)
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("RESULT");
                Log.d("MainActivity", "Результат: " + result);
                // Дополнительно показываем Toast для наглядности
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        };

        // 2. Создаём и запускаем фоновый поток MyLooper
        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        // Ждём, пока Handler в MyLooper будет готов (необязательно, но надёжнее)
        myLooper.waitUntilReady();

        // 3. Обработчик кнопки
        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем введённые данные
                String ageStr = binding.editTextAge.getText().toString();
                String job = binding.editTextJob.getText().toString();

                if (ageStr.isEmpty() || job.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Заполните оба поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age = Integer.parseInt(ageStr);

                // Создаём сообщение для отправки в фоновый поток
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("AGE", age);
                bundle.putString("JOB", job);
                msg.setData(bundle);

                // Отправляем сообщение в очередь MyLooper
                myLooper.mHandler.sendMessage(msg);

                Log.d("MainActivity", "Сообщение отправлено, задержка " + age + " секунд");
                Toast.makeText(MainActivity.this, "Сообщение отправлено. Ждите " + age + " сек.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}