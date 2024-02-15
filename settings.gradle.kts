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
        create("common") {
            from(files("versions-common/libs.versions.toml"))
        }
    }
}

rootProject.name = "android-gradle-catalogs"

include(
    ":versions-androidx",
    ":versions-common",
    ":versions-compose"
)

include(":android-plugins")
include(":settings-plugin")
