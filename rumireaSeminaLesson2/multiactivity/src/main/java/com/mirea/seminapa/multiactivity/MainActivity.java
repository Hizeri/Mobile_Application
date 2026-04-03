package com.mirea.seminapa.multiactivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //обязательный вызов родительского метода
        setContentView(R.layout.activity_main);
    }

    public void onClickSendData(View view) {
        EditText editText = findViewById(R.id.editTextInput);
        String userData = editText.getText().toString();

        // Создаём Intent для вызова SecondActivity
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("USER_DATA", userData); // кладём данные в Intent (ключ, значение)
        startActivity(intent);
    }
}