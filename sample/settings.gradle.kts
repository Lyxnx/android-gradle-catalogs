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
        create("common") {
            from("io.github.lyxnx:versions-common:2023.08.02")
        }
        create("androidx") {
            from("io.github.lyxnx:versions-androidx:2023.08.02")
        }
        create("compose") {
            from("io.github.lyxnx:versions-compose:2023.08.02")
        }
    }
}

rootProject.name = "sampleapp"
include(":app")
