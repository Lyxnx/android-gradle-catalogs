plugins {
//    id(common.plugins.android.application.get().pluginId)
//    id(common.plugins.kotlin.android.get().pluginId)

    id("com.android.application") version "8.1.0"
//    id("org.jetbrains.kotlin.android") version "1.9.0"

    id("io.github.lyxnx.android-room-config")
}

android {
    namespace = "io.github.lyxnx.sampleapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "io.github.lyxnx.sampleapp"
        minSdk = 26
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

roomConfig {
    androidxCatalog.set("asdfasdfasdf")
}

//
dependencies {
//    implementation(androidx.core.ktx)
//    implementation(androidx.appcompat)
//    implementation(common.material)
//
//    testImplementation(common.junit4)
//    androidTestImplementation(androidx.test.ext.junit.ktx)
//    androidTestImplementation(androidx.test.espresso)

}