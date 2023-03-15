# Android Gradle Catalogs

This repo contains some useful gradle version catalogs:

- [AndroidX](versions-androidx/libs.versions.toml) - Regular AndroidX artifacts
- [Accompanist](versions-accompanist/libs.versions.toml) - Google Accompanist Compose artifacts
- [Jetpack Compose](versions-compose/libs.versions.toml) - Jetpack Compose artifacts
- [Common Android](versions-common/libs.versions.toml) - Regular Android artifacts, such as gradle plugins, hilt
- [Firebase](versions-firebase/libs.versions.toml) - Firebase BOM with included artifacts

## Using

There are 2 methods of using:

For both, version catalogs are created within the `dependencyResolutionManagement` block of the `settings.gradle.kts` file:

```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("CATALOGNAME") {
            from(/* either local or remote ID here */)
        }
    }
}
```

### Local/submodule

Either clone or submodule this repository to the root of the project and reference it in `settings.gradle.kts`

The repository does not need to be included as the gradle files can be ignored and the catalog directories are prefixed with `versions-`

```
.
├── app/
├── android-gradle-catalogs/
│   ├──
├── build.gradle.kts
└── settings.gradle.kts
```

### GitHub package registry

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
        // + any others
    }
}
```
