import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    id(common.plugins.vanniktech.publish.get().pluginId)
}

dependencies {
    compileOnly(common.android.gradleplugin)
    compileOnly(common.kotlin.gradleplugin)
    compileOnly(common.ksp.gradleplugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

tasks.withType(KotlinCompile::class).configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

group = "io.github.lyxnx.gradle.android"
version = project.providers.gradleProperty("catalogs.version").get()
description = "Various plugins relating to the android gradle catalogs project"

mavenPublishing {
    coordinates(project.group.toString(), project.name, project.version.toString())

    publishToMavenCentral(SonatypeHost.Companion.S01, true)
    signAllPublications()

    pom {
        name.set("Android Catalogs Plugins")
        description.set(project.description)
        inceptionYear.set("2023")
        url.set("https://github.com/Lyxnx/android-gradle-catalogs")
        licenses {
            license {
                name.set("MIT License")
                url.set("http://www.opensource.org/licenses/mit-license.php")
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
        register("room-config") {
            id = "io.github.lyxnx.gradle.android.room-config"
            displayName = "Android Room configuration plugin"
            description = "Configures RoomDB for an Android application or library"
            implementationClass = "io.github.lyxnx.gradle.android.catalogs.room.RoomConfigPlugin"
        }

        register("compose-config") {
            id = "io.github.lyxnx.gradle.android.compose-config"
            displayName = "Android Compose configuration plugin"
            description = "Configures Compose for an Android application or library"
            implementationClass = "io.github.lyxnx.gradle.android.catalogs.compose.ComposeConfigPlugin"
        }

        register("compose-compiler-config") {
            id = "io.github.lyxnx.gradle.android.compose-compiler-config"
            displayName = "Android Compose compiler configuration plugin"
            description = "Configures the Compose compiler for an Android application or library"
            implementationClass = "io.github.lyxnx.gradle.android.catalogs.compose.ComposeCompilerConfigPlugin"
        }

        register("hilt-config-ksp") {
            id = "io.github.lyxnx.gradle.android.hilt-config-ksp"
            displayName = "Android Hilt configuration plugin using KSP"
            description = "Configures Hilt for an Android application or library using KSP"
            implementationClass = "io.github.lyxnx.gradle.android.catalogs.hilt.HiltKSPConfigPlugin"
        }

        register("hilt-config-kapt") {
            id = "io.github.lyxnx.gradle.android.hilt-config-kapt"
            displayName = "Android Hilt configuration plugin using KAPT"
            description = "Configures Hilt for an Android application or library using KAPT"
            implementationClass = "io.github.lyxnx.gradle.android.catalogs.hilt.HiltKAPTConfigPlugin"
        }

        register("firebase-config") {
            id = "io.github.lyxnx.gradle.android.firebase-config"
            displayName = "Android Firebase configuration plugin"
            description = "Configures Firebase for an Android application or library"
            implementationClass = "io.github.lyxnx.gradle.android.catalogs.firebase.FirebaseConfigPlugin"
        }
    }
}
