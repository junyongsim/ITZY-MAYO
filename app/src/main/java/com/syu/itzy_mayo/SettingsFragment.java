package com.syu.itzy_mayo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    
    private FirebaseAuth mAuth;
    private UserSessionManager sessionManager;
    private LinearLayout notLoggedInView;
    private LinearLayout loggedInView;
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button githubLoginButton;
    private Button registerButton;
    private Button logoutButton;
    private TextView userEmail;
    private FirebaseAuth.AuthStateListener authStateListener;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        // UserSessionManager 초기화
        sessionManager = UserSessionManager.getInstance(requireContext());
        mAuth = sessionManager.getFirebaseAuth();


        // UI 컴포넌트 초기화
        initializeViews(rootView);
        
        // 로그인 상태 UI 업데이트
        updateLoginUI();
        
        // 각 설정 항목에 대한 클릭 리스너 설정
        setupSettingsClickListeners(rootView);
        super.onStart();
        setupAuthStateListener();
        mAuth.addAuthStateListener(authStateListener);

        return rootView;

    }
    
    @Override
    public void onStart() {
        super.onStart();
        // 앱 시작 시 로그인 상태 확인
        updateLoginUI();
    }
    private void setupAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    sessionManager.createLoginSession(user);
                    Toast.makeText(getContext(), "로그인 성공: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    updateLoginUI();
                    clearInputFields();
                } else {
                    // 로그아웃 상태일 때도 필요하다면 처리
                    updateLoginUI();
                }
            }
        };
    }
    private void initializeViews(View rootView) {
        notLoggedInView = rootView.findViewById(R.id.not_logged_in_view);
        loggedInView = rootView.findViewById(R.id.logged_in_view);
        emailInput = rootView.findViewById(R.id.email_input);
        passwordInput = rootView.findViewById(R.id.password_input);
        loginButton = rootView.findViewById(R.id.login_button);
        githubLoginButton = rootView.findViewById(R.id.github_login_button);
        registerButton = rootView.findViewById(R.id.register_button);
        logoutButton = rootView.findViewById(R.id.logout_button);
        userEmail = rootView.findViewById(R.id.user_email);
        
        // 버튼 클릭 리스너 설정
        loginButton.setOnClickListener(v -> loginUser());
        githubLoginButton.setOnClickListener(v -> loginUserWithGithub());
        registerButton.setOnClickListener(v -> registerUser());
        logoutButton.setOnClickListener(v -> logoutUser());
    }
    
    private void updateLoginUI() {
        if (sessionManager.isLoggedIn()) {
            // 로그인 상태 UI
            notLoggedInView.setVisibility(View.GONE);
            loggedInView.setVisibility(View.VISIBLE);
            userEmail.setText(sessionManager.getUserEmail());
        } else {
            // 로그아웃 상태 UI
            notLoggedInView.setVisibility(View.VISIBLE);
            loggedInView.setVisibility(View.GONE);
        }
    }
    
    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // 세션 생성
                        sessionManager.createLoginSession(user);
                        Toast.makeText(getContext(), "로그인 성공: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        updateLoginUI();
                        clearInputFields();
                    } else {
                        Toast.makeText(getContext(), "로그인 실패: " + task.getException().getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    private void loginUserWithGithub() {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("user:email");
                    }
                };
        provider.setScopes(scopes);
        mAuth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                .addOnFailureListener(requireActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "로그인 실패: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                        }
                    }
                );

    }
    
    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // 세션 생성
                        sessionManager.createLoginSession(user);
                        Toast.makeText(getContext(), "회원가입 성공: " + user.getEmail(), 
                                Toast.LENGTH_SHORT).show();
                        updateLoginUI();
                        clearInputFields();
                    } else {
                        Toast.makeText(getContext(), "회원가입 실패: " + task.getException().getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    
    private void logoutUser() {
        sessionManager.logoutUser();
        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
        updateLoginUI();
    }
    
    private void clearInputFields() {
        emailInput.setText("");
        passwordInput.setText("");
    }

    private void setupSettingsClickListeners(View rootView) {
        TextView notificationSettings = rootView.findViewById(R.id.notification_settings);
        TextView accountSettings = rootView.findViewById(R.id.account_settings);
        TextView aboutApp = rootView.findViewById(R.id.about_app);

        notificationSettings.setOnClickListener(v -> Toast.makeText(getContext(), "알림 설정", Toast.LENGTH_SHORT).show());

        accountSettings.setOnClickListener(v -> {
            // 계정 설정은 로그인 상태에서만 접근 가능
            if (sessionManager.isLoggedIn()) {
                Toast.makeText(getContext(), "계정 설정", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        aboutApp.setOnClickListener(v -> Toast.makeText(getContext(), "앱 정보", Toast.LENGTH_SHORT).show());
    }
}