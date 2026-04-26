package com.mirea.seminapa.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {

    public Handler mHandler;           // Handler, связанный с этим потоком (для приёма сообщений)
    private Handler mainHandler;       // Handler главного потока (для отправки ответа)
    private boolean isReady = false;   // флаг готовности Handler

    public MyLooper(Handler mainThreadHandler) {
        this.mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Log.d("MyLooper", "run: подготовка Looper");
        Looper.prepare();                       // создаём очередь сообщений для этого потока

        // Создаём Handler, который будет обрабатывать входящие сообщения в этом потоке
        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // Получаем данные из сообщения
                Bundle data = msg.getData();
                int age = data.getInt("AGE");
                String job = data.getString("JOB");

                Log.d("MyLooper", "Получено: возраст=" + age + ", профессия=" + job);

                // Имитация задержки = возраст секунд
                try {
                    Thread.sleep(age * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Формируем результат
                String result = "Возраст " + age + " лет, профессия: " + job + ". Задержка составила " + age + " секунд.";

                // Отправляем результат обратно в главный поток через mainHandler
                Bundle resultBundle = new Bundle();
                resultBundle.putString("RESULT", result);
                Message resultMsg = new Message();
                resultMsg.setData(resultBundle);
                mainHandler.sendMessage(resultMsg);
            }
        };

        isReady = true;   // Handler готов к приёму сообщений
        Log.d("MyLooper", "run: запуск Looper.loop()");
        Looper.loop();    // запускаем бесконечный цикл обработки сообщений
        // этот метод блокируется, пока не вызовут looper.quit()
    }

    // Метод для ожидания готовности Handler (чтобы не отправлять сообщения до инициализации)
    public void waitUntilReady() {
        while (!isReady) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}