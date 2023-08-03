plugins {
    `kotlin-dsl`
    id(common.plugins.vanniktech.publish.get().pluginId)
}

dependencies {
    compileOnly(common.android.gradleplugin)
    compileOnly(common.ksp.gradleplugin)
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
