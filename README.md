# Android Gradle Catalogs

This repo contains some useful gradle version catalogs, namely [Compose Accompanist](accompanist.versions.toml) 
and [AndroidX](accompanist.versions.toml)

Simply submodule this repo somewhere within the main project and point to it within `settings.gradle.kts` as such:

```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("androidx") {
            from(files("android-gradle-catalogs/androidx.versions.toml"))
        }
        create("accompanist") {
            from(files("android-gradle-catalogs/accompanist.versions.toml"))
        }
    }
}
```

For example the project structure could be
```
├── android-gradle-catalogs
│   ├── androidx.versions.toml
│   └── accompanist.versions.toml
├── gradle
│   ├── wrapper
│   └── libs.versions.toml
├── app
├── settings.gradle.kts
...
```

with the `settings.gradle.kts` file consisting of the above code.

In this example, the `libs` catalog does not need to be specified as that is implicitly included by gradle