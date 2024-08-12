# Android Gradle Catalogs <GitHub path="Lyxnx/android-gradle-catalogs" />

[![Version](https://img.shields.io/maven-central/v/io.github.lyxnx.gradle/versions-shared?style=flat-square)][mavenCentral]
[![License](https://img.shields.io/github/license/Lyxnx/android-gradle-catalogs?style=flat-square)][license]

Version catalogs and plugins to help reduce boilerplate in Android Gradle build scripts.

> :warning: The catalogs and accompanying plugins are meant for Kotlin DSL - they have not been tested with Groovy DSL

---

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Version Catalogs](#version-catalogs)
  - [Using](#using)
- [Plugins](#plugins)
  - [Compose](#compose)
    - [Compiler](#compiler)
    - [UI](#ui)
  - [Room](#room)
    - [Configuring](#configuring)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Version Catalogs

This repo contains some useful gradle version catalogs specific to Android:

| Name                                             | Description                                                                                                    |
|--------------------------------------------------|----------------------------------------------------------------------------------------------------------------|
| [Shared](versions-shared/libs.versions.toml)     | All artifacts that don't belong in any of the other catalogs. This is the largest one and thus the most useful | 
| [AndroidX](versions-androidx/libs.versions.toml) | AndroidX artifacts only                                                                                        |

### Using

This project comes with a plugin (`io.github.lyxnx.gradle.android-catalogs`) that can be applied to
the `settings.gradle.kts` file and will apply the `androidx` and `shared` catalogs to the project, since those are
almost guaranteed to be used in any Android project

```kotlin
// <root>/settings.gradle.kts
plugins {
    id("io.github.lyxnx.gradle.android-catalogs") version "<version>"
}
```

After applying the plugin, the compose catalog can be applied to the project by adding the following to
the `settings.gradle.kts` file:

```kotlin
// <root>/settings.gradle.kts
dependencyResolutionManagement {
    versionCatalogs {
        compose()
        // If a catalog would like to be configured globally:
        shared {
            version("kotlin", "1.7.20")
        }
    }
}
```

Alternatively, catalogs can be created manually within the `dependencyResolutionManagement` block of
the `settings.gradle.kts` file:

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

The gradle catalogs are published to the Maven Central repo, so only the standard `mavenCentral()` definition in the
repositories block is required

For the common catalog, it would be as follows, replacing `versions-shared` with any of the required version names:

```kotlin
// <root>/settings.gradle.kts
dependencyResolutionManagement {
    versionCatalogs {
        create("shared") {
            from("io.github.lyxnx.gradle:versions-shared:<version>")
        }
    }
}
```

To use, reference it within a build file:

```kotlin
// Eg. app/build.gradle.kts
dependencies {
    // For com.google.code.gson:gson
    implementation(shared.gson)
}
```

When using plugins, alias them within the root `build.gradle.kts` file:

```kotlin
// <root>/build.gradle.kts
plugins {
    alias(shared.plugins.android.application) apply false
    alias(shared.plugins.android.library) apply false
    alias(shared.plugins.kotlin.android) apply false
}
```

The plugins can then be used within another module:

```kotlin
// Eg. app/build.gradle.kts
plugins {
    id(shared.plugins.android.application.get().pluginId)
    // OR
    id("com.android.application")
}
```

For more information on version catalogs, see
the [Gradle User Guide](https://docs.gradle.org/current/userguide/platforms.html)

## Plugins

This repository comes with a few Gradle plugins for Android that use the catalogs

Ensure each required plugin is referenced in the root build file, but not applied:

```kotlin
// <root>/build.gradle.kts
plugins {
    // for just the basic compiler and runtime setup - useful for modules that use Compose tools, such as state but not the full UI
    id("io.github.lyxnx.android-compose-compiler") version "<version>" apply false
    // for modules that use the full Compose UI, if using this, the compiler plugin is applied in addition
    id("io.github.lyxnx.android-compose-ui") version "<version>" apply false
    id("io.github.lyxnx.android-room") version "<version>" apply false
}
```

As these plugins are Android specific, they require the `com.android.application`
or `com.android.library` plugin to be added to the module, depending on the module type.

### Compose

Compose configuration consists of 2 plugins: `io.github.lyxnx.android-compose-compiler`
and `io.github.lyxnx.android-compose-ui`

#### Compiler

This applies basic Jetpack Compose configuration options to an application or library module, although this would
typically be used on its own in a library module:

1. Enables the compose build feature
2. Sets the compose compiler version to the same one defined by the first catalog containing
   the `androidx.compose.compiler:compiler` artifact
3. Adds the compose BOM for versioning to the dependencies
4. Adds the compose runtime dependency

This plugin also adds the ability to generate compose compiler reports and compiler metrics by specifying a property in
a project's properties file, or on the command line for a singular task:

**For a singular task:**

For compiler reports:
`./gradlew :mymodule:assembleRelease -Pcatalogs.composeCompilerReports=true`

or for metrics:
`./gradlew :mymodule:assembleRelease -Pcatalogs.composeCompilerMetrics=true`

Alternatively, both can be combined to generate both types, although compiler reports are much more useful than the
metrics.

**For a module:**

To use in a singular module place the properties in each module's `gradle.properties` file or the global file to apply
to all modules.

In either way, the report/metric output directory can be configured using the `catalogs.composeCompilerReportsDir`
property.

Note that this property is relative to the current module. So placing it in the global properties file will
result in that path being relative to the current module in the build process.

If not given, it will default to `<current module>/build/compose-compiler-reports`

#### UI

Applies the compiler plugin in addition to the following:

1. Adds the compose ui, foundation, and tooling preview dependencies
2. Adds the ui testing dependencies

### Room

This plugin requires the [KSP](https://github.com/google/ksp) (ID `com.google.devtools.ksp`) plugin to be applied to the
module in addition to the standard Android plugins

This plugin applies RoomDB configuration options to the application or library module:

1. Applies the KSP plugin if needed
2. Sets the schema directory to the configured directory (see below)
3. Adds the Room runtime, runtime ktx, and compiler to dependencies from the first catalog containing
   the `androidx.room:room-runtime`, `androidx.room:room-ktx`, `androidx.room:room-compiler`, and
   the `androidx.room:room-testing` artifacts
4. Adds the Room testing dependency

#### Configuring

This has an extension function for configuring the schema directory if the default does not wish to be used:

```kotlin
// Eg. app/build.gradle.kts
import io.github.lyxnx.gradle.android.catalogs.room.roomSchemaDir

roomSchemaDir(file("somewhere/schema_dir"))
// OR
roomSchemaDir("somewhere/schema_dir")
```

Note that this file is relative to the current build script.
The above would result in the schema directory being `app/somewhere/schema_dir` for the `:app` module

[mavenCentral]: https://search.maven.org/artifact/io.github.lyxnx.gradle/versions-shared

[license]: LICENCE
