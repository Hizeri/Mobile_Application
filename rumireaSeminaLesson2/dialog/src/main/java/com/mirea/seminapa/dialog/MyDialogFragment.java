package com.mirea.seminapa.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  //создаём строитель и передаём контекст активности
        builder.setTitle("Здравствуй МИРЭА!")
                .setMessage("Успех близок?")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Иду дальше", (dialog, id) -> {
                    ((MainActivity) getActivity()).onOkClicked(); //приводим к типу MainActivity
                    dialog.cancel();
                })
                .setNeutralButton("На паузе", (dialog, id) -> {
                    ((MainActivity) getActivity()).onNeutralClicked();
                    dialog.cancel();
                })
                .setNegativeButton("Нет", (dialog, id) -> {
                    ((MainActivity) getActivity()).onCancelClicked();
                    dialog.cancel();
                });
        // Создаём объект AlertDialog и возвращаем его
        return builder.create();
    }
}