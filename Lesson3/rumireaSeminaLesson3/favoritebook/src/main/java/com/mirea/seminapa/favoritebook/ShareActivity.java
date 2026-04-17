package com.mirea.seminapa.favoritebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        EditText etBook = findViewById(R.id.etUserBook);
        Button btnSend = findViewById(R.id.btnSendBack);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = etBook.getText().toString().trim();
                if (bookName.isEmpty()) {
                    bookName = "Неизвестно";
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("user_book_key", bookName);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
