package com.mirea.seminapa.workmanager;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import com.mirea.seminapa.workmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStartWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаём задачу (WorkRequest)
                WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                        .build();

                // Запускаем задачу
                WorkManager.getInstance(MainActivity.this).enqueue(uploadWorkRequest);
            }
        });
    }
}