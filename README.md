# Android Gradle Catalogs <GitHub path="Lyxnx/android-gradle-catalogs" />

[![Version](https://img.shields.io/maven-central/v/io.github.lyxnx.gradle/versions-common?style=flat-square)][mavenCentral]
[![License](https://img.shields.io/github/license/Lyxnx/android-gradle-catalogs?style=flat-square)][license]

Version catalogs and plugins to help reduce boilerplate in Android Gradle build scripts.

> :warning: The catalogs and accompanying plugins are meant for Kotlin DSL - they have not been tested with Groovy DSL

---

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Version Catalogs](#version-catalogs)
  - [Using](#using)
  - [Updating](#updating)
- [Plugins](#plugins)
  - [Catalogs Config](#catalogs-config)
  - [Compose Config](#compose-config)
  - [Room Config](#room-config)
    - [Configuring](#configuring)
  - [Hilt Config](#hilt-config)
  - [Firebase Config](#firebase-config)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Version Catalogs

This repo contains some useful gradle version catalogs specific to Android:

| Name                                                   | Description                                                                                     |
|--------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| [Common](versions-common/libs.versions.toml)           | Regular Android artifacts, such as gradle plugins, and some of the most commonly used libraries |
| [AndroidX](versions-androidx/libs.versions.toml)       | AndroidX artifacts only                                                                         |
| [Compose](versions-compose/libs.versions.toml)         | Jetpack Compose artifacts (including the BOM) - useful with the compose config plugin below     |
| [Accompanist](versions-accompanist/libs.versions.toml) | Google Accompanist Compose artifacts (including the BOM)                                        |
| [Firebase](versions-firebase/libs.versions.toml)       | Firebase artifacts (including the BOM) - useful with the firebase config plugin below           |

The `bleeding` branch receives version updates as soon as they are available, including unstable
alpha versions, and is not guaranteed to be compatible with gradle/android, or other versions.
Versions can be overridden in `settings.gradle.kts` though

The `master` branch receives fewer version updates but contains no in-dev versions and only stable releases

### Using

Version catalogs are created within the `dependencyResolutionManagement` block of the `settings.gradle.kts` file:

```kotlin
// <root>/settings.gradle.kts
dependencyResolutionManagement {
    versionCatalogs {
        create("CATALOGNAME") {
            from(/* Remote ID here */)
            // If a different version is required, it can be overridden:
            // For example, to override kotlin to 1.7.20
            version("kotlin", "1.7.20")
        }
    }
}
```

The gradle catalogs are publishing in the Maven Central repo, so only the standard `mavenCentral()` definition in the
repositories block is required

For the common catalog, it would be as follows, replacing `versions-common` with any of the required version names:

```kotlin
// <root>/settings.gradle.kts
dependencyResolutionManagement {
    versionCatalogs {
        create("common") {
            from("io.github.lyxnx:versions-common:<version>")
        }
    }
}
```

To use, reference it within a build file:

```kotlin
// Eg. app/build.gradle.kts
dependencies {
    // For com.google.code.gson:gson
    implementation(common.gson)
}
```

When using plugins, alias them within the root `build.gradle.kts` file:

```kotlin
// <root>/build.gradle.kts
plugins {
    alias(common.plugins.android.application) apply false
    alias(common.plugins.android.library) apply false
    alias(common.plugins.kotlin.android) apply false
}
```

The plugins can then be used within another module:

```kotlin
// Eg. app/build.gradle.kts
plugins {
    id(common.plugins.android.application.get().pluginId)
    // OR
    id("com.android.application")
}
```

For more information on version catalogs, see
the [Gradle User Guide](https://docs.gradle.org/current/userguide/platforms.html)

### Updating

The artifact versions within each catalog are updated automatically by
the [Renovate Bot](https://github.com/renovatebot/renovate) as soon as they become available to
the [bleeding](https://github.com/Lyxnx/android-gradle-catalogs/tree/bleeding) branch

As such, no manual intervention is needed and any new artifacts or catalogs added will be automatically updated too

## Plugins

This repository comes with a few Gradle plugins for Android that use the catalogs

Ensure each required plugin is referenced in the root build file, but not applied:

```kotlin
// <root>/build.gradle.kts
plugins {
    id("io.github.lyxnx.android-compose-config") version "<version>" apply false
    id("io.github.lyxnx.android-room-config") version "<version>" apply false
    id("io.github.lyxnx.android-hilt-config") version "<version>" apply false
    id("io.github.lyxnx.android-firebase-config") version "<version>" apply false

    /*
    This plugin is only required for customising catalogs from the root build file and will need to 
    be applied here unlike the rest of them
    
    See below for more information
     */
    id("io.github.lyxnx.android-catalogs-config") version "<version>"
}
```

As these plugins are Android specific, they require the `com.android.application`
or `com.android.library` plugin to be added to the module, depending on the module type.

### Catalogs Config

This plugin has no functionality of its own and is only used to allow customisation of catalog names used by the other
plugins from the root build file:

```kotlin
// <root>/build.gradle.kts
/*
For example, if you wish to change the name of the AndroidX catalog from the default "androidx" to "someothercatalog"

Now any time a plugin, such as the Room config plugin, references the AndroidX catalog, it will use this name instead
 */
androidCatalogPlugins {
    androidxCatalogName.set("someothercatalog")
}
```

### Compose Config

This plugin applies Jetpack Compose configuration options to the application or library module:

1. Enables the compose build feature
2. Sets the compose compiler version to the same as one in the `compose` catalog
3. Adds the compose BOM for versioning to the dependencies
4. Adds the compose runtime, ui, foundation, and tooling preview dependencies
5. Sets up the dependencies for UI tests

### Room Config

This plugin requires the [KSP](https://github.com/google/ksp) (ID `com.google.devtools.ksp`) plugin to be applied to the
module in addition to the standard Android plugins

This plugin applies RoomDB configuration options to the application or library module:

1. Applies the KSP plugin if needed
2. Sets the schema directory to the configured directory (see below)
3. Adds the Room runtime, runtime ktx, and compiler to dependencies from the `androidx` catalog
4. Adds the Room testing dependency

#### Configuring

This has a section for configuring the schema directory if the default does not wish to be used:

```kotlin
// Eg. app/build.gradle.kts
androidCatalogPlugins {
    roomConfig {
        schemaDir.set(file("somewhere/schema_dir"))
    }
}
```

Note that this file is relative to the current build script.
The above would result in the schema directory being `app/somewhere/schema_dir`

### Hilt Config

This plugin requires the [KAPT](https://kotlinlang.org/docs/kapt.html) (ID `org.jetbrains.kotlin.kapt`) and
[Dagger Hilt](https://dagger.dev/hilt/gradle-setup.html) (ID `dagger.hilt.android.plugin`) plugins to be applied to the
module in addition to the standard Android plugins

This plugin adds the standard hilt library and the compiler to the dependencies from the `common` catalog

### Firebase Config

This plugin requires the [Google Services](https://developers.google.com/android/guides/google-services-plugin)
(ID `com.google.gms.google-services`) plugin to be applied to the module in addition to the standard Android plugins

This plugin is simple and only performs the following:

1. Adds the Google services plugin if needed
2. Adds the Firebase BOM to the dependencies from the `firebase` catalog

[mavenCentral]: https://search.maven.org/artifact/io.github.lyxnx.gradle/versions-common

[license]: LICENCE
