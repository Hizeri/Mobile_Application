package com.mirea.seminapa.simplefragmentapp;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // При первом запуске покажем первый фрагмент
        if (savedInstanceState == null) {
            loadFragment(new FirstFragment());
        }
    }

    public void onClickFragment(View view) {
        Fragment fragment = null;  //временная переменная для хранения фрагмента
        // Определяем, какая кнопка была нажата, по её ID
        if (view.getId() == R.id.btnFirst) {
            fragment = new FirstFragment();
        } else if (view.getId() == R.id.btnSecond) {
            fragment = new SecondFragment();
        }

        // Если фрагмент был создан, загружаем его в контейнер
        if (fragment != null) {
            loadFragment(fragment);
        }
    }

    //для замены фрагмента
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); // Получаем FragmentManager  и начинаем транзакцию
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}