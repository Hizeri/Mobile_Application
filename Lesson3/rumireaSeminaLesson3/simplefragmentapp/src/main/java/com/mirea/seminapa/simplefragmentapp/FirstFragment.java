package com.mirea.seminapa.simplefragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflater.inflate() – превращает XML-разметку в объект View
        // container –  куда будет помещён фрагмент)
        // false – не добавлять автоматически созданный View в container (это сделает система позже)
        return inflater.inflate(R.layout.fragment_first, container, false);
    }
}