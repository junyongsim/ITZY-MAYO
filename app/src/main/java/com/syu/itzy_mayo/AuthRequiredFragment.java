package com.syu.itzy_mayo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 인증이 필요한 Fragment의 기본 클래스
 * 사용자가 로그인하지 않은 경우 설정 화면으로 리디렉션
 */
public abstract class AuthRequiredFragment extends Fragment {
    
    protected UserSessionManager sessionManager;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = UserSessionManager.getInstance(requireContext());
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 로그인 상태 확인
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(requireContext(), "로그인이 필요한 기능입니다.", Toast.LENGTH_SHORT).show();
            // 설정 화면으로 이동
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_frame_layout, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }
    
    /**
     * 로그인 상태 변경 감지를 위한 메서드
     * 로그아웃 시 Fragment를 종료하도록 구현
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!sessionManager.isLoggedIn()) {
            // 로그아웃 상태에서 접근한 경우 설정 화면으로 이동
            Toast.makeText(requireContext(), "로그인이 필요한 기능입니다.", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
    }
} 