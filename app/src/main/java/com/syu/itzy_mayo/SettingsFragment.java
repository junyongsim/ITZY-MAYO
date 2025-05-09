package com.syu.itzy_mayo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        // 각 설정 항목에 대한 클릭 리스너 설정
        setupSettingsClickListeners(rootView);

        return rootView;
    }

    private void setupSettingsClickListeners(View rootView) {
        TextView notificationSettings = rootView.findViewById(R.id.notification_settings);
        TextView accountSettings = rootView.findViewById(R.id.account_settings);
        TextView aboutApp = rootView.findViewById(R.id.about_app);

        notificationSettings.setOnClickListener(v -> Toast.makeText(getContext(), "알림 설정", Toast.LENGTH_SHORT).show());

        accountSettings.setOnClickListener(v -> Toast.makeText(getContext(), "계정 설정", Toast.LENGTH_SHORT).show());

        aboutApp.setOnClickListener(v -> Toast.makeText(getContext(), "앱 정보", Toast.LENGTH_SHORT).show());
    }
}