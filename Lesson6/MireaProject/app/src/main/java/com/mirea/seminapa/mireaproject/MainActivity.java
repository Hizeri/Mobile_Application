package com.mirea.seminapa.mireaproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    // Настройка верхней панели и бокового меню
    private AppBarConfiguration mAppBarConfiguration;

    // Это контейнер для бокового меню. Благодаря ему меню может выдвигаться сбоку
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Подключаем главный layout activity_main.xml
        setContentView(R.layout.activity_main);

        // Находим Toolbar и делаем его верхней панелью приложения
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Находим DrawerLayout и NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Находим NavHostFragment — контейнер, куда будут подставляться фрагменты
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        // Получаем NavController, который управляет переходами между фрагментами
        NavController navController = navHostFragment.getNavController();

        // Настраиваем верхнюю панель и добавляем все основные пункты меню
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_data,
                R.id.nav_webview,
                R.id.nav_background_task,
                R.id.nav_accelerometer,
                R.id.nav_camera,
                R.id.nav_audio,
                R.id.nav_profile,
                R.id.nav_file_work,
                R.id.nav_weather,
                R.id.nav_places)
                .setOpenableLayout(drawerLayout)
                .build();

        // Связываем Toolbar с NavController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Связываем боковое меню с NavController
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Снова получаем NavController из NavHostFragment
        NavController navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment)).getNavController();

        // Обрабатываем кнопку "назад" или кнопку открытия меню
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}