# Android Gradle Catalogs

This repo contains some useful gradle version catalogs:

- [AndroidX](versions-androidx/libs.versions.toml) - Regular AndroidX artifacts
- [Accompanist](versions-accompanist/libs.versions.toml) - Google Accompanist Compose artifacts
- [Jetpack Compose](versions-compose/libs.versions.toml) - Jetpack Compose artifacts
- [Common Android](versions-common/libs.versions.toml) - Regular Android artifacts, such as gradle plugins, hilt

## Using

Create version catalogs within the `dependencyResolutionManagement` block of the `settings.gradle.kts` file.

This will require a GitHub username and personal access token added to either the project or
global `gradle.properties` file.

```kotlin
dependencyResolutionManagement {
    repositories {
        maven("https://maven.pkg.github.com/Lyxnx/android-gradle-catalogs") {
            credentials {
                username = providers.gradleProperty("gpr.username").get()
                password = providers.gradleProperty("gpr.token").get()
            }
        }
    }
    versionCatalogs {
        create("androidx") {
            from("net.lyxnx.android:versions-androidx:2023.03.01")
        }
        create("compose") {
            from("net.lyxnx.android:versions-compose:2023.03.01")
        }
    }
}
```
