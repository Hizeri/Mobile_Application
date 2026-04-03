package com.mirea.seminapa.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.widget.Toast;

public class MyProgressDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Загрузка");
        dialog.setMessage("Пожалуйста, подождите...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // крутящийся индикатор
        dialog.setCancelable(false);

        // закрыть через 2 секунды
        new Handler().postDelayed(() -> { //  выполнить код после задержки
            if (dialog.isShowing()) {
                dialog.dismiss();  // закрывает диалог

                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Загрузка завершена", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000);

        return dialog;
    }
}