package com.mirea.seminapa.buttonclicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvOut;
    private Button btnWhoAmI;
    private Button btnItsNotMe;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = findViewById(R.id.tvOut);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItsNotMe = findViewById(R.id.btnItsNotMe);
        checkBox = findViewById(R.id.checkBox);

        // Обработчик для btnWhoAmI
        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOut.setText("22");
                checkBox.toggle(); // переключаем чекбокс
            }
        };
        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);

        // Обработчик для btnItIsNotMe уже через атрибут onClick
    }

    public void onItsNotMeClick(View view) {
        tvOut.setText("Это не я");
        checkBox.toggle(); // переключаем чекбокс
    }
}