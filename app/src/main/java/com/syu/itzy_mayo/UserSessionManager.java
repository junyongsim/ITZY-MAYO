package com.syu.itzy_mayo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * 사용자 로그인 상태를 관리하는 유틸리티 클래스
 * 로그인 상태를 저장하고 확인하는 기능 제공
 */
public class UserSessionManager {
    private static final String TAG = "UserSessionManager";
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    
    private static UserSessionManager instance;
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final FirebaseAuth mAuth;
    
    private UserSessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        mAuth = FirebaseAuth.getInstance();
    }
    
    public static synchronized UserSessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserSessionManager(context.getApplicationContext());
        }
        return instance;
    }
    
    /**
     * 로그인 세션을 생성하고 사용자 정보를 저장
     * @param user Firebase 사용자 객체
     */
    public void createLoginSession(FirebaseUser user) {
        if (user != null) {
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putString(KEY_USER_ID, user.getUid());
            editor.putString(KEY_USER_EMAIL, user.getEmail());
            editor.apply();
            Log.d(TAG, "로그인 세션 생성: " + user.getEmail());
        }
    }
    
    /**
     * 로그인 상태 체크
     * @return 로그인 상태 (true: 로그인, false: 로그아웃)
     */
    public boolean isLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean isFirebaseLoggedIn = currentUser != null;
        boolean isLocalLoggedIn = pref.getBoolean(KEY_IS_LOGGED_IN, false);
        
        // Firebase와 로컬 저장소의 로그인 상태가 일치하지 않는 경우 동기화
        if (isFirebaseLoggedIn != isLocalLoggedIn) {
            if (isFirebaseLoggedIn) {
                createLoginSession(currentUser);
            } else {
                logoutUser();
            }
        }
        
        return isFirebaseLoggedIn;
    }
    
    /**
     * 로그아웃 처리
     */
    public void logoutUser() {
        mAuth.signOut();
        
        // 저장된 사용자 데이터 삭제
        editor.clear();
        editor.apply();
        
        Log.d(TAG, "사용자 로그아웃");
    }
    
    /**
     * 현재 사용자의 이메일 가져오기
     * @return 로그인된 사용자 이메일, 로그인되지 않은 경우 null
     */
    public String getUserEmail() {
        if (isLoggedIn()) {
            return mAuth.getCurrentUser().getEmail();
        }
        return null;
    }
    
    /**
     * 현재 사용자의 ID 가져오기
     * @return 로그인된 사용자 ID, 로그인되지 않은 경우 null
     */
    public String getUserId() {
        if (isLoggedIn()) {
            return mAuth.getCurrentUser().getUid();
        }
        return null;
    }
    
    /**
     * Firebase 인증 객체 가져오기
     * @return Firebase 인증 객체
     */
    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }
} 