package com.mirea.seminapa.mireaproject;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

public class PlacesFragment extends Fragment {

    private MapView mapView;
    private CompassOverlay compassOverlay;
    private ScaleBarOverlay scaleBarOverlay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Загружаем настройки osmdroid
        Configuration.getInstance().load(
                requireContext().getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext())
        );

        View view = inflater.inflate(R.layout.fragment_places, container, false);

        mapView = view.findViewById(R.id.mapViewPlaces);

        // Стандартная карта OpenStreetMap
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        // Включаем кнопки масштаба и жесты двумя пальцами
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setZoomRounding(true);

        // Центр карты — Москва
        GeoPoint startPoint = new GeoPoint(55.751574, 37.573856);
        mapView.getController().setZoom(12.0);
        mapView.getController().setCenter(startPoint);

        // Добавляем заведения
        addPlaceMarker(
                new GeoPoint(55.752023, 37.617499),
                "ГУМ",
                "Адрес: Красная площадь, 3",
                "Исторический торговый комплекс рядом с Красной площадью."
        );

        addPlaceMarker(
                new GeoPoint(55.760186, 37.618711),
                "Большой театр",
                "Адрес: Театральная площадь, 1",
                "Один из самых известных театров России."
        );

        addPlaceMarker(
                new GeoPoint(55.758633, 37.606888),
                "Пушкинская площадь",
                "Адрес: Пушкинская площадь",
                "Популярное место встреч в центре Москвы."
        );

        // Функция по работе с картой на выбор: компас
        addCompass();

        // Дополнительно добавим шкалу масштаба
        addScaleBar();

        return view;
    }

    private void addPlaceMarker(GeoPoint point, String title, String address, String description) {
        Marker marker = new Marker(mapView);

        // Координаты маркера
        marker.setPosition(point);

        // Название заведения
        marker.setTitle(title);

        // Описание, которое будет видно в стандартном окне маркера
        marker.setSubDescription(address + "\n" + description);

        // Иконка маркера
        marker.setIcon(ResourcesCompat.getDrawable(
                getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on,
                null
        ));

        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        // При нажатии на маркер показываем адрес и краткое описание
        marker.setOnMarkerClickListener((clickedMarker, mapView) -> {
            Toast.makeText(
                    requireContext(),
                    clickedMarker.getTitle() + "\n" + clickedMarker.getSubDescription(),
                    Toast.LENGTH_LONG
            ).show();

            clickedMarker.showInfoWindow();

            return true;
        });

        mapView.getOverlays().add(marker);
    }

    private void addCompass() {
        compassOverlay = new CompassOverlay(
                requireContext().getApplicationContext(),
                new InternalCompassOrientationProvider(requireContext().getApplicationContext()),
                mapView
        );

        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);
    }

    private void addScaleBar() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(displayMetrics.widthPixels / 2, 10);

        mapView.getOverlays().add(scaleBarOverlay);
    }

    @Override
    public void onResume() {
        super.onResume();

        Configuration.getInstance().load(
                requireContext().getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext())
        );

        if (mapView != null) {
            mapView.onResume();
        }

        if (compassOverlay != null) {
            compassOverlay.enableCompass();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Configuration.getInstance().save(
                requireContext().getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext())
        );

        if (compassOverlay != null) {
            compassOverlay.disableCompass();
        }

        if (mapView != null) {
            mapView.onPause();
        }
    }
}