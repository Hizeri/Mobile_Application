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

    private TextView tvStatus;
    private Button btnRecord, btnPlay;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private String fileName;
    private static final int REQUEST_RECORD_AUDIO = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        tvStatus = view.findViewById(R.id.tvAudioStatus);
        btnRecord = view.findViewById(R.id.btnRecord);
        btnPlay = view.findViewById(R.id.btnPlay);

        fileName = requireContext().getFilesDir().getAbsolutePath() + "/recording.3gp";

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO);
        }

        btnRecord.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Нет разрешения на запись", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isRecording) stopRecording();
            else startRecording();
        });

        btnPlay.setOnClickListener(v -> {
            File file = new File(fileName);
            if (!file.exists() || file.length() == 0) {
                Toast.makeText(getContext(), "Нет записи", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isPlaying) stopPlaying();
            else startPlaying();
        });

        return view;
    }

    private void startRecording() {
        if (recorder != null) recorder.release();
        new File(fileName).delete();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
            isRecording = true;
            btnRecord.setText("Остановить запись");
            btnPlay.setEnabled(false);
            tvStatus.setText("Запись...");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (RuntimeException e) { }
            recorder.release();
            recorder = null;
        }
        isRecording = false;
        btnRecord.setText("Начать запись");
        btnPlay.setEnabled(true);
        tvStatus.setText("Запись завершена");
    }

    private void startPlaying() {
        if (player != null) player.release();
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            isPlaying = true;
            btnPlay.setText("Остановить воспроизведение");
            btnRecord.setEnabled(false);
            tvStatus.setText("Воспроизведение...");
            player.setOnCompletionListener(mp -> stopPlaying());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlaying() {
        if (player != null) {
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
        if (recorder != null) recorder.release();
        if (player != null) player.release();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Разрешение на запись получено", Toast.LENGTH_SHORT).show();
        }
    }
}