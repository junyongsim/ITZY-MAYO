package com.syu.itzy_mayo;


import android.app.Application;

import com.naver.maps.map.NaverMapSdk;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 네이버 지도 SDK 초기화
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NcpKeyClient(BuildConfig.NCP_CLIENT_ID));
    }
}