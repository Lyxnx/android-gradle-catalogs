import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    id(common.plugins.vanniktech.publish.get().pluginId)
}

dependencies {
    compileOnly(common.android.gradleplugin)
    compileOnly(common.ksp.gradleplugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType(KotlinCompile::class).configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

group = "io.github.lyxnx.gradle.android"
version = "1.0"

mavenPublishing {

}

gradlePlugin {
    plugins {
        register("room-config") {
            id = "io.github.lyxnx.android-room-config"
            displayName = "Android Room configuration plugin"
            description = "Configures RoomDB for an Android application or library"
            implementationClass = "io.github.lyxnx.gradle.android.AndroidRoomConfigPlugin"
        }
    }
}
