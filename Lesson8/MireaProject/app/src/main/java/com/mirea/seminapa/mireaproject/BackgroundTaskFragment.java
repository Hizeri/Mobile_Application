package com.mirea.seminapa.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.mirea.seminapa.mireaproject.databinding.FragmentBackgroundTaskBinding;

public class BackgroundTaskFragment extends Fragment {
    private FragmentBackgroundTaskBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Подключаем layout через ViewBinding
        binding = FragmentBackgroundTaskBinding.inflate(inflater, container, false);

        // Возвращаем корневой элемент разметки, чтобы Fragment отобразился на экране
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Обработчик кнопки запуска фоновой задачи
        binding.buttonRun.setOnClickListener(v -> {
            String input = binding.editTextNumber.getText().toString();
            // Проверяем, что поле не пустое
            if (input.isEmpty()) {
                Toast.makeText(getContext(), "Введите число", Toast.LENGTH_SHORT).show();
                return;
            }

            // Переводим строку в число
            int n = Integer.parseInt(input);
            binding.textViewResult.setText("Вычисление запущено...");
            // Создаём данные для передачи в Worker
            Data inputData = new Data.Builder()
                    .putInt("N", n)
                    .build();

            // Создаём одноразовую задачу WorkManager
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SumWorker.class)
                    .setInputData(inputData)
                    .build();

            // Ставим задачу в очередь выполнения
            WorkManager.getInstance(requireContext()).enqueue(workRequest);

            // Наблюдаем за состоянием задачи
            WorkManager.getInstance(requireContext())
                    .getWorkInfoByIdLiveData(workRequest.getId())
                    .observe(getViewLifecycleOwner(), new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {

                            // Если задача завершилась успешно
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                binding.textViewResult.setText("Вычисления завершены успешно!");
                                Toast.makeText(getContext(), "Worker выполнен", Toast.LENGTH_SHORT).show();

                                // Если задача завершилась ошибкой
                            } else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                                binding.textViewResult.setText("Ошибка при выполнении");
                                Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Очищаем binding, чтобы не было утечки памяти
        binding = null;
    }
}