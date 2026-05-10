package com.mirea.seminapa.audiorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.mirea.seminapa.audiorecord.databinding.ActivityMainBinding;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int PERMISSION_CODE = 200;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private String fileName;
    private static final String TAG = "AudioRecord";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Путь к файлу – внутренняя папка приложения (не требует разрешения на запись)
        fileName = new File(getFilesDir(), "audio_record.3gp").getAbsolutePath();
        Log.d(TAG, "Файл будет сохранён: " + fileName);

        // Запрос разрешения RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_CODE);
        }

        binding.buttonRecord.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Нет разрешения на запись", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isRecording) {
                stopRecording();
            } else {
                startRecording();
            }
        });

        binding.buttonPlay.setOnClickListener(v -> {
            if (isPlaying) {
                stopPlaying();
            } else {
                startPlaying();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Разрешение на запись получено", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Разрешение не получено", Toast.LENGTH_SHORT).show();
        }
    }

    private void startRecording() {
        if (recorder != null) {
            recorder.release();
        }
        File file = new File(fileName);
        if (file.exists()) file.delete();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // MPEG_4 вместо THREE_GPP
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);    // AAC вместо AMR_NB

        try {
            recorder.prepare();
            recorder.start();
            isRecording = true;
            binding.buttonRecord.setText("Остановить запись");
            binding.buttonPlay.setEnabled(false);
            binding.statusText.setText("Идёт запись...");
            Log.d(TAG, "Запись начата");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (RuntimeException e) {
                // Возможна ошибка, если файл пустой или запись не началась
                e.printStackTrace();
                Toast.makeText(this, "Ошибка остановки записи", Toast.LENGTH_SHORT).show();
            }
            recorder.release();
            recorder = null;
        }
        isRecording = false;
        binding.buttonRecord.setText("Начать запись");
        binding.buttonPlay.setEnabled(true);
        binding.statusText.setText("Запись завершена");
        Log.d(TAG, "Запись остановлена, файл существует: " + new File(fileName).exists());
    }

    private void startPlaying() {
        if (player != null) {
            player.release();
        }
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            isPlaying = true;
            binding.buttonPlay.setText("Остановить воспроизведение");
            binding.buttonRecord.setEnabled(false);
            binding.statusText.setText("Воспроизведение...");
            Log.d(TAG, "Воспроизведение начато, длина звука: " + player.getDuration() + " мс");
            player.setOnCompletionListener(mp -> {
                stopPlaying();
                binding.statusText.setText("Воспроизведение завершено");
            });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Ошибка воспроизведения: " + e.getMessage());
            Toast.makeText(this, "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        isPlaying = false;
        binding.buttonPlay.setText("Воспроизвести");
        binding.buttonRecord.setEnabled(true);
        binding.statusText.setText("Готов к записи");
        Log.d(TAG, "Воспроизведение остановлено");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recorder != null) recorder.release();
        if (player != null) player.release();
    }
}