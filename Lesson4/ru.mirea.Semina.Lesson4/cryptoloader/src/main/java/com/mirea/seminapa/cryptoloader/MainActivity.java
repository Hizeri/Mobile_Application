package com.mirea.seminapa.cryptoloader;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.mirea.seminapa.cryptoloader.databinding.ActivityMainBinding;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private ActivityMainBinding binding;
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonEncrypt.setOnClickListener(v -> {
            String input = binding.editTextPhrase.getText().toString();
            if (input.isEmpty()) {
                Toast.makeText(this, "Введите фразу", Toast.LENGTH_SHORT).show();
                return;
            }
            SecretKey key = generateKey();          // генерируем ключ
            byte[] encrypted = encryptMsg(input, key); // шифруем

            Bundle args = new Bundle();
            args.putByteArray(MyLoader.ARG_WORD, encrypted);
            args.putByteArray("key", key.getEncoded());

            LoaderManager.getInstance(this).restartLoader(LOADER_ID, args, this);
        });
    }

    // Генерация ключа (AES, 256 бит)
    private SecretKey generateKey() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new SecretKeySpec(kg.generateKey().getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Шифрование текста
    private byte[] encryptMsg(String message, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Вызывается, когда Loader создаётся
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Toast.makeText(this, "Создание загрузчика...", Toast.LENGTH_SHORT).show();
        return new MyLoader(this, args);
    }

    // Вызывается, когда Loader завершил работу (расшифровал)
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (data != null && !data.isEmpty()) {
            Toast.makeText(this, "Расшифрованная фраза: " + data, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Ошибка расшифровки", Toast.LENGTH_SHORT).show();
        }
    }

    // Сброс Loader (не используется)
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}
}