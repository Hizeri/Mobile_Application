package com.mirea.seminapa.mireaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraFragment extends Fragment {

    private ImageView ivPhoto; // ImageView для отображения сделанного фото
    private Button btnTakePhoto; // кнопка запуска камеры
    private Uri imageUri; // путь к файлу фотографии
    private ActivityResultLauncher<Intent> cameraLauncher; // запуск камеры и получение результата

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Подключаем разметку fragment_camera.xml
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ivPhoto = view.findViewById(R.id.ivPhoto);
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto);

        // Регистрируем обработчик результата после съёмки
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Если фото сделано успешно, показываем его в ImageView
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        ivPhoto.setImageURI(imageUri);
                    } else {
                        Toast.makeText(getContext(), "Съёмка отменена", Toast.LENGTH_SHORT).show();
                    }
                });

        // Обработчик кнопки "Сделать фото"
        btnTakePhoto.setOnClickListener(v -> {
            // Проверяем разрешение на камеру
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                // Если разрешения нет — запрашиваем его
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
            } else {
                // Если разрешение есть — запускаем камеру
                dispatchTakePictureIntent();
            }
        });

        return view;
    }

    // Метод запуска системной камеры
    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Проверяем, есть ли приложение камеры на устройстве
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            try {
                // Создаём файл для фотографии
                File photoFile = createImageFile();

                // Получаем authority для FileProvider
                String authority = requireContext().getPackageName() + ".fileprovider";

                // Получаем безопасный Uri для передачи файла камере
                imageUri = FileProvider.getUriForFile(requireContext(), authority, photoFile);

                // Передаём камере путь, куда сохранить фото
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                // Запускаем камеру
                cameraLauncher.launch(intent);

            } catch (IOException e) {
                Toast.makeText(getContext(), "Ошибка создания файла", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Камера не найдена", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод создания файла для фотографии
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Получаем папку Pictures внутри папки приложения
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Создаём временный jpg-файл
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    // Обработка ответа пользователя на запрос разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Если разрешение на камеру выдано — запускаем камеру
        if (requestCode == 101
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            Toast.makeText(getContext(), "Нет разрешения на камеру", Toast.LENGTH_SHORT).show();
        }
    }
}