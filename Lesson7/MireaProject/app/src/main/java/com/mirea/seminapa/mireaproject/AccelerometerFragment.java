package com.mirea.seminapa.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccelerometerFragment extends Fragment implements SensorEventListener {

    // Менеджер для работы с датчиками устройства
    private SensorManager sensorManager;

    // Сам датчик акселерометра
    private Sensor accelerometer;

    // TextView для вывода значений по осям
    private TextView tvX, tvY, tvZ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Подключаем layout фрагмента
        View view = inflater.inflate(R.layout.fragment_accelerometer, container, false);

        // Находим TextView для вывода X, Y, Z
        tvX = view.findViewById(R.id.tvX);
        tvY = view.findViewById(R.id.tvY);
        tvZ = view.findViewById(R.id.tvZ);

        // Получаем системный сервис датчиков
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);

        // Получаем акселерометр устройства
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Возвращаем готовый экран фрагмента
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Когда фрагмент активен, начинаем слушать акселерометр
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Когда фрагмент неактивен, отключаем датчик
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Проверяем, что данные пришли именно от акселерометра
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // Выводим значения ускорения по осям X, Y, Z
            tvX.setText("X: " + event.values[0]);
            tvY.setText("Y: " + event.values[1]);
            tvZ.setText("Z: " + event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Метод обязателен для интерфейса, но здесь не используется
    }
}