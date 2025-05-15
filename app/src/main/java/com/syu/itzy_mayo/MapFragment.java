package com.syu.itzy_mayo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private FusedLocationProviderClient fusedLocationClient;

    public MapFragment() {
        super(R.layout.fragment_map);
    }

    // 권한 요청 런처
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    initMap();
                } else {
                    showMissingPermissionError();
                }
            });

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 권한 요청을 위한 LocationSource 초기화
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // FusedLocationProviderClient 초기화 (현재 위치 가져오기 위함)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // 지도 초기화
        initMap();
    }

    private void initMap() {
        FragmentManager fm = getChildFragmentManager();
        com.naver.maps.map.MapFragment mapFragment = (com.naver.maps.map.MapFragment) fm.findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = com.naver.maps.map.MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        // UI 설정
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setScaleBarEnabled(true);
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setLocationButtonEnabled(true);

        // 위치 추적 모드 및 소스 설정
        naverMap.setLocationSource(locationSource);

        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            showCurrentLocation();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 서울시청에 기본 마커 추가
        addMarker();
    }

    private void addMarker() {
        // 서울시청 좌표
        LatLng seoulCityHall = new LatLng(37.566826, 126.978656);

        // 마커 객체 생성
        Marker marker = new Marker();
        marker.setPosition(seoulCityHall);
        marker.setIcon(OverlayImage.fromResource(R.drawable.ic_marker));
        marker.setWidth(80);
        marker.setHeight(80);
        marker.setCaptionText("서울시청");
        marker.setCaptionColor(Color.BLACK);
        marker.setCaptionHaloColor(Color.WHITE);
        marker.setCaptionTextSize(16);
        marker.setMap(naverMap);

        // 지도 이동 (필요시)
        naverMap.moveCamera(CameraUpdate.scrollTo(seoulCityHall));
    }

    private void showCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        // FusedLocationProviderClient로 현재 위치 가져오기
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null && naverMap != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // 현재 위치에 마커 추가
                            addCurrentLocationMarker(latitude, longitude);

                            // 카메라 이동
                            LatLng currentPosition = new LatLng(latitude, longitude);
                            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(currentPosition);
                            naverMap.moveCamera(cameraUpdate);
                        }
                    }
                });
    }

    private void addCurrentLocationMarker(double latitude, double longitude) {
        // 현재 위치 좌표
        LatLng currentPosition = new LatLng(latitude, longitude);

        // 마커 객체 생성
        Marker marker = new Marker();
        marker.setPosition(currentPosition);
        marker.setIcon(OverlayImage.fromResource(R.drawable.ic_my_location));
        marker.setWidth(80);
        marker.setHeight(80);
        marker.setMap(naverMap);
    }

    private void showMissingPermissionError() {
        Toast.makeText(getContext(), "Location permission is required to show your position.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
