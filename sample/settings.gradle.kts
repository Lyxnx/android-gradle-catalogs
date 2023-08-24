@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        val catalogVersion = "2023.08.19"
        create("common") {
            from("io.github.lyxnx.gradle:versions-common:$catalogVersion")
        }
        create("androidx") {
            from("io.github.lyxnx.gradle:versions-androidx:$catalogVersion")
        }
        create("compose") {
            from("io.github.lyxnx.gradle:versions-compose:$catalogVersion")
        }
    }
}

rootProject.name = "sampleapp"
include(":app")
