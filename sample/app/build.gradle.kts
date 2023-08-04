plugins {
    id(common.plugins.android.application.get().pluginId)
    id(common.plugins.kotlin.android.get().pluginId)

    id("io.github.lyxnx.android-room-config")
}

android {
    namespace = "io.github.lyxnx.sampleapp"
    compileSdk = 33

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

//roomConfig {
//    androidxCatalog.set("asdfasdfasdf")
//    schemaDir.set(file("asdf"))
//}

dependencies {
    implementation(androidx.core.ktx)
    implementation(androidx.appcompat)
    implementation(common.material)

    testImplementation(common.junit4)
    androidTestImplementation(androidx.test.ext.junit.ktx)
    androidTestImplementation(androidx.test.espresso)
}
