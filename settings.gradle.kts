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
}

rootProject.name = "android-gradle-catalogs"

includeBuild("catalogs-plugin")

include(
    ":versions-accompanist",
    ":versions-androidx",
    ":versions-common",
    ":versions-compose",
    ":versions-firebase"
)
