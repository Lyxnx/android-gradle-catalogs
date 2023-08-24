plugins {
    id(common.plugins.android.application.get().pluginId)
    id(common.plugins.kotlin.android.get().pluginId)
    id(common.plugins.ksp.get().pluginId)

    id("io.github.lyxnx.android-compose-config")
}

android {
    namespace = "io.github.lyxnx.sampleapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.lyxnx.sampleapp"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(androidx.core.ktx)
    implementation(androidx.appcompat)
    implementation(common.material)

    implementation(androidx.activity.compose)
    implementation(compose.material3)

    testImplementation(common.junit4)
    androidTestImplementation(androidx.test.ext.junit.ktx)
    androidTestImplementation(androidx.test.espresso)
}
