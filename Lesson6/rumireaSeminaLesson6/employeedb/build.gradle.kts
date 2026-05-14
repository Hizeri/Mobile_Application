plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mirea.seminapa.employeedb"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.mirea.seminapa.employeedb"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Room — библиотека для работы с базой данных SQLite
    implementation("androidx.room:room-runtime:2.6.1")

    // Компилятор Room, он обрабатывает аннотации @Entity, @Dao, @Database
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}