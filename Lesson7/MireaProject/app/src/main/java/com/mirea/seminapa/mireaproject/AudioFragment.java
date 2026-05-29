package com.mirea.seminapa.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

public class AudioFragment extends Fragment {

    private TextView tvStatus;          // текстовый статус записи/воспроизведения
    private Button btnRecord, btnPlay;  // кнопки записи и воспроизведения

    private MediaRecorder recorder;     // объект для записи звука
    private MediaPlayer player;         // объект для воспроизведения звука

    private boolean isRecording = false; // идёт ли запись
    private boolean isPlaying = false;   // идёт ли воспроизведение

    private String fileName;             // путь к файлу записи
    private static final int REQUEST_RECORD_AUDIO = 200; // код запроса разрешения

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Подключаем разметку fragment_audio.xml
        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        // Находим элементы интерфейса
        tvStatus = view.findViewById(R.id.tvAudioStatus);
        btnRecord = view.findViewById(R.id.btnRecord);
        btnPlay = view.findViewById(R.id.btnPlay);

        // Создаём путь к файлу записи во внутренней папке приложения
        fileName = requireContext().getFilesDir().getAbsolutePath() + "/recording.3gp";

        // Проверяем разрешение на запись аудио
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Если разрешения нет — запрашиваем его
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO);
        }

        // Обработчик кнопки записи
        btnRecord.setOnClickListener(v -> {

            // Повторно проверяем разрешение перед записью
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Нет разрешения на запись", Toast.LENGTH_SHORT).show();
                return;
            }

            // Если запись идёт
            if (isRecording) {
                stopRecording();
            } else {
                startRecording();
            }
        });

        // Обработчик кнопки воспроизведения
        btnPlay.setOnClickListener(v -> {

            // Проверяем, существует ли файл записи
            File file = new File(fileName);
            if (!file.exists() || file.length() == 0) {
                Toast.makeText(getContext(), "Нет записи", Toast.LENGTH_SHORT).show();
                return;
            }

            // Если воспроизведение идёт
            if (isPlaying) {
                stopPlaying();
            } else {
                startPlaying();
            }
        });

        return view;
    }

    // Начало записи звука
    private void startRecording() {

        // Если старый recorder существует — освобождаем его
        if (recorder != null) {
            recorder.release();
        }

        // Удаляем старый файл записи
        new File(fileName).delete();

        // Создаём новый MediaRecorder
        recorder = new MediaRecorder();

        // Источник звука — микрофон
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        // Формат выходного файла
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        // Куда сохранять запись
        recorder.setOutputFile(fileName);

        // Кодек аудио
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            // Подготовка и запуск записи
            recorder.prepare();
            recorder.start();

            // Обновляем состояние и интерфейс
            isRecording = true;
            btnRecord.setText("Остановить запись");
            btnPlay.setEnabled(false);
            tvStatus.setText("Запись...");

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
        }
    }

    // Остановка записи
    private void stopRecording() {

        if (recorder != null) {
            try {
                // Останавливаем запись
                recorder.stop();
            } catch (RuntimeException e) {
                // Ошибка возможна, если запись была слишком короткой
            }

            // Освобождаем ресурсы микрофона
            recorder.release();
            recorder = null;
        }

        // Обновляем состояние и интерфейс
        isRecording = false;
        btnRecord.setText("Начать запись");
        btnPlay.setEnabled(true);
        tvStatus.setText("Запись завершена");
    }

    // Начало воспроизведения
    private void startPlaying() {

        // Если старый player существует — освобождаем его
        if (player != null) {
            player.release();
        }

        // Создаём новый MediaPlayer
        player = new MediaPlayer();

        try {
            // Указываем файл для воспроизведения
            player.setDataSource(fileName);

            // Подготавливаем и запускаем воспроизведение
            player.prepare();
            player.start();
            isPlaying = true;
            btnPlay.setText("Остановить воспроизведение");
            btnRecord.setEnabled(false);
            tvStatus.setText("Воспроизведение...");

            // Когда запись закончится, остановить воспроизведение
            player.setOnCompletionListener(mp -> stopPlaying());

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
        }
    }

    // Остановка воспроизведения
    private void stopPlaying() {

        if (player != null) {
            // Останавливаем и освобождаем плеер
            player.stop();
            player.release();
            player = null;
        }

        isPlaying = false;
        btnPlay.setText("Воспроизвести");
        btnRecord.setEnabled(true);
        tvStatus.setText("Готов");
    }

    @Override
    public void onStop() {
        super.onStop();

        // Освобождаем ресурсы при уходе с фрагмента
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Проверяем результат запроса разрешения на микрофон
        if (requestCode == REQUEST_RECORD_AUDIO
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Разрешение на запись получено", Toast.LENGTH_SHORT).show();
        }
    }
}