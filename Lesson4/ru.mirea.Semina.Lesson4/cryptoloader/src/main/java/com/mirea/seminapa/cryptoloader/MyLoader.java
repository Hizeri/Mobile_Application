package com.mirea.seminapa.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {

    public static final String ARG_WORD = "encrypted_data";
    private byte[] encryptedData;
    private byte[] secretKeyBytes;

    public MyLoader(@NonNull Context context, @Nullable Bundle args) {
        super(context);
        if (args != null) {
            encryptedData = args.getByteArray(ARG_WORD);
            secretKeyBytes = args.getByteArray("key");
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad(); // запускаем loadInBackground
    }

    @Override
    public String loadInBackground() {
        if (encryptedData == null || secretKeyBytes == null) return null;
        try {
            SecretKey originalKey = new SecretKeySpec(secretKeyBytes, "AES");
            return decryptMsg(encryptedData, originalKey);
        } catch (Exception e) {
            Log.e("MyLoader", "decrypt error", e);
            return null;
        }
    }

    private String decryptMsg(byte[] cipherText, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(cipherText));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}