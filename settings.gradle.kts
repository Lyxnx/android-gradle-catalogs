@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("shared") {
            from(files("versions-shared/libs.versions.toml"))
        }
    }
}

rootProject.name = "android-gradle-catalogs"

include(
    ":versions-androidx",
    ":versions-shared"
)

include(":android-plugins")
include(":settings-plugin")
