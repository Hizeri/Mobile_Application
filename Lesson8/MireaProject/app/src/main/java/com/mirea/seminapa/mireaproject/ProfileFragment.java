package com.mirea.seminapa.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    // Имя файла SharedPreferences
    private static final String PREFS_NAME = "profile_settings";

    // Ключи для сохранения данных
    private static final String KEY_NAME = "NAME";
    private static final String KEY_GROUP = "GROUP";
    private static final String KEY_FAVORITE = "FAVORITE";

    private EditText editTextName;
    private EditText editTextGroup;
    private EditText editTextFavorite;
    private TextView textViewProfileResult;
    private Button buttonSaveProfile;

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Находим элементы интерфейса
        editTextName = view.findViewById(R.id.editTextName);
        editTextGroup = view.findViewById(R.id.editTextGroup);
        editTextFavorite = view.findViewById(R.id.editTextFavorite);
        textViewProfileResult = view.findViewById(R.id.textViewProfileResult);
        buttonSaveProfile = view.findViewById(R.id.buttonSaveProfile);

        // Получаем файл настроек профиля
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // При открытии фрагмента сразу загружаем сохранённые данные
        loadProfile();

        // По кнопке сохраняем данные
        buttonSaveProfile.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void saveProfile() {
        // Получаем текст из полей
        String name = editTextName.getText().toString();
        String group = editTextGroup.getText().toString();
        String favorite = editTextFavorite.getText().toString();

        // Проверяем заполнение
        if (name.isEmpty() || group.isEmpty() || favorite.isEmpty()) {
            Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Открываем редактор SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Сохраняем данные по ключам
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_GROUP, group);
        editor.putString(KEY_FAVORITE, favorite);

        // Применяем изменения
        editor.apply();

        // Обновляем текст на экране
        textViewProfileResult.setText(
                "Имя: " + name + "\nГруппа: " + group + "\nЛюбимое: " + favorite);

        Toast.makeText(getContext(), "Профиль сохранён", Toast.LENGTH_SHORT).show();
    }

    private void loadProfile() {
        // Читаем сохранённые значения
        String name = sharedPreferences.getString(KEY_NAME, "");
        String group = sharedPreferences.getString(KEY_GROUP, "");
        String favorite = sharedPreferences.getString(KEY_FAVORITE, "");

        // Подставляем данные обратно в поля
        editTextName.setText(name);
        editTextGroup.setText(group);
        editTextFavorite.setText(favorite);

        // Если данные уже были сохранены, показываем их
        if (!name.isEmpty() || !group.isEmpty() || !favorite.isEmpty()) {
            textViewProfileResult.setText(
                    "Имя: " + name + "\nГруппа: " + group + "\nЛюбимое: " + favorite);
        }
    }
}