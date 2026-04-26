package com.mirea.seminapa.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;

public class WebViewFragment extends Fragment {

    private WebView webView; // Объект WebView – встроенный браузер

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        webView = view.findViewById(R.id.webView);

        // Включаем поддержку JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        // Чтобы ссылки открывались внутри WebView, а не в браузере
        webView.setWebViewClient(new WebViewClient());
        // Загружаем страницу по умолчанию
        webView.loadUrl("https://petstory.ru/knowledge/cats/");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webView != null) {
            webView.destroy();
        }
    }
}