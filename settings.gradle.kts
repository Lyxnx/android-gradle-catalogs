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
        create("accompanist") {
            from(files("versions-accompanist/libs.versions.toml"))
        }
        create("androidx") {
            from(files("versions-androidx/libs.versions.toml"))
        }
        create("common") {
            from(files("versions-common/libs.versions.toml"))
        }
        create("compose") {
            from(files("versions-compose/libs.versions.toml"))
        }
        create("firebase") {
            from(files("versions-firebase/libs.versions.toml"))
        }
    }
}

rootProject.name = "android-gradle-catalogs"

includeBuild("catalog-extensions")
include(
    ":versions-accompanist",
    ":versions-androidx",
    ":versions-common",
    ":versions-compose",
    ":versions-firebase"
)
include(":test")
