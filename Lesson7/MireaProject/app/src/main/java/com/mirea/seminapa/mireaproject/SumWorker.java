package com.mirea.seminapa.mireaproject;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class SumWorker extends Worker {

    public SumWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        int n = getInputData().getInt("N", 10);
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
            try {
                TimeUnit.MILLISECONDS.sleep(200); // имитация долгой работы
            } catch (InterruptedException e) {
                return Result.failure();
            }
        }
        Log.d("SumWorker", "Сумма от 1 до " + n + " = " + sum);
        return Result.success(); // // Сообщаем WorkManager, что задача выполнена успешно
    }
}