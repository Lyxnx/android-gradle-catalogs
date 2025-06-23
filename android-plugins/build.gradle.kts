import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    `kotlin-dsl`
    id(shared.plugins.vanniktech.publish.get().pluginId)
}

dependencies {
    compileOnly(shared.android.gradleplugin)
    compileOnly(shared.kotlin.gradleplugin)
    compileOnly(shared.kotlin.compose.gradleplugin)
    compileOnly(shared.jetbrains.compose.gradleplugin)
    compileOnly(shared.ksp.gradleplugin)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
    jvmToolchain(17)
}

group = "io.github.lyxnx.gradle.android"
version = project.providers.gradleProperty("plugins.version").get()
description = "Various plugins relating to the android gradle catalogs project"

mavenPublishing {
    coordinates(project.group.toString(), project.name, project.version.toString())
    publishToMavenCentral(true)
    signAllPublications()

    pom {
        name.set("Android Catalogs Plugins")
        description.set(project.description)
        inceptionYear.set("2023")
        url.set("https://github.com/Lyxnx/android-gradle-catalogs")
        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("https://opensource.org/license/apache-2-0")
            }
        }
        developers {
            developer {
                id.set("Lyxnx")
                name.set("Lyxnx")
                url.set("https://github.com/Lyxnx")
            }
        }
        scm {
            url.set("https://github.com/Lyxnx/android-gradle-catalogs")
            connection.set("https://github.com/Lyxnx/android-gradle-catalogs.git")
            developerConnection.set("scm:git:ssh://git@github.com/Lyxnx/android-gradle-catalogs.git")
        }
    }
}

gradlePlugin {
    plugins {
        register("compose-compiler") {
            id = "io.github.lyxnx.gradle.android-compose-compiler"
            displayName = "Android Compose compiler configuration plugin"
            description = "Configures the Compose compiler for an Android application or library"
            implementationClass = "io.github.lyxnx.gradle.android.catalogs.compose.ComposeCompilerPlugin"
        }

        register("compose-ui") {
            id = "io.github.lyxnx.gradle.android-compose-ui"
            displayName = "Android Compose UI configuration plugin"
            description = "Configures Compose for an Android application or library"
            implementationClass = "io.github.lyxnx.gradle.android.catalogs.compose.ComposeUIPlugin"
        }

        register("room") {
            id = "io.github.lyxnx.gradle.android-room"
            displayName = "Android Room configuration plugin"
            description = "Configures RoomDB for an Android application or library"
            implementationClass = "io.github.lyxnx.gradle.android.catalogs.room.RoomPlugin"
        }
    }
}
