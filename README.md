# Android Gradle Catalogs

This repo contains some useful gradle version catalogs:

- [AndroidX](versions-androidx/libs.versions.toml) - Regular AndroidX artifacts
- [Accompanist](versions-accompanist/libs.versions.toml) - Google Accompanist Compose artifacts
- [Jetpack Compose](versions-compose/libs.versions.toml) - Jetpack Compose artifacts
- [Common Android](versions-common/libs.versions.toml) - Regular Android artifacts, such as gradle plugins, hilt
- [Firebase](versions-firebase/libs.versions.toml) - Firebase BOM with included artifacts

The `bleeding` branch receives version updates as soon as they are available and is not guaranteed to be compatible with 
gradle/android, or other versions. Versions can be overridden in `settings.gradle.kts` though

The `master` branch receives fewer version updates but is stable and versions will work together

## Using

Version catalogs are created within the `dependencyResolutionManagement` block of the `settings.gradle.kts` file:

```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("CATALOGNAME") {
            from(/* Either local or remote ID here */)
            // If a different version is required, it can be overridden:
            version("kotlin", "1.7.20")
        }
    }
}
```

Either clone or submodule this repository to the root of the project and reference it in `settings.gradle.kts`

The repository does not need to be included like a regular module, since the gradle files can be ignored 
and only the catalog directories prefixed with `versions-` need to be referenced 

For example, the project structure could be as such:
```
.
├── app/
├── android-gradle-catalogs/
│   ├── versions-accompanist/
│   ├── versions-androidx/
│   ├── versions-common/
│   ├── versions-compose/
│   └── versions-firebase/ 
├── build.gradle.kts
└── settings.gradle.kts
```

and within the `settings.gradle.kts` file:
```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("common") {
            from(files("android-gradle-catalogs/versions-common/libs.versions.toml"))
        }
        create("androidx") {
            from(files("android-gradle-catalogs/versions-androidx/libs.versions.toml"))
        }
        // + any others
    }
}
```

and then in a module's `build.gradle.kts` file:
```kotlin
dependencies {
    // Example for androidx.activity:activity-ktx
    implementation(androidx.activity.ktx)
}
```

When using plugins, alias them within the root `build.gradle.kts` file:
```kotlin
plugins {
    alias(common.plugins.android.application) apply false
    alias(common.plugins.android.library) apply false
    alias(common.plugins.kotlin.android) apply false
}
```