// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    alias(common.plugins.android.application) apply false
//    alias(common.plugins.kotlin.android) apply false

//    alias(common.plugins.ksp) apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
    id("io.github.lyxnx.android-room-config") version "1.0" apply false
}