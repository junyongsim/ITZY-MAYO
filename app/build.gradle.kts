import java.util.Properties
import java.io.FileInputStream


plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

val localProperties = Properties().apply {
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

android {
    namespace = "com.syu.itzy_mayo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.syu.itzy_mayo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // 네이버 지도 클라이언트 ID (local.properties에서 가져옴)
        buildConfigField("String", "NCP_CLIENT_ID",
            "\"${localProperties.getProperty("NCP_CLIENT_ID", "")}\""
        )
        buildConfigField("String", "SUPER_BASE_URL",
        "\"${localProperties.getProperty("SUPER_BASE_URL", "")}\""
        )
        buildConfigField("String", "SUPER_BASE_ANON_KEY",
        "\"${localProperties.getProperty("SUPER_BASE_ANON_KEY", "")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.naver.maps:map-sdk:3.21.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}