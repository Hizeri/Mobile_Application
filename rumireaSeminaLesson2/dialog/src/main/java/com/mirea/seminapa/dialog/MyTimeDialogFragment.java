package com.mirea.seminapa.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();  // Получаем текущее время
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //извлекает текущий час в 24часовом формате
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (view, hourOfDay, minuteOfHour) -> {
            String time = hourOfDay + ":" + minuteOfHour;
            Toast.makeText(getActivity(), "Выбрано время: " + time, Toast.LENGTH_LONG).show();
        }, hour, minute, true);
    }
}