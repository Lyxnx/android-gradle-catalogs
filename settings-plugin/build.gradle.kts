plugins {
    `kotlin-dsl`
    id(shared.plugins.vanniktech.publish.get().pluginId)
}

kotlin {
    jvmToolchain(17)
}

group = "io.github.lyxnx.gradle.android"
description = "A Gradle settings plugin to automatically apply version catalogs"
version = project.providers.gradleProperty("catalogs.version").get()

gradlePlugin {
    plugins {
        register("standard-catalogs") {
            id = "io.github.lyxnx.gradle.catalogs"
            displayName = "Gradle catalogs plugin"
            description = "Applies basic gradle catalogs automatically"
            implementationClass = "io.github.lyxnx.gradle.catalogs.CatalogsPlugin"
        }

        register("android-catalogs") {
            id = "io.github.lyxnx.gradle.android-catalogs"
            displayName = "Android catalogs plugin"
            description = "Applies all android catalogs automatically"
            implementationClass = "io.github.lyxnx.gradle.catalogs.android.AndroidCatalogsPlugin"
        }
    }
}

val generatedResourceDir = layout.buildDirectory.dir("generated-resources")
val writeProps = tasks.register<WriteProperties>("writeProps") {
    destinationFile.set(generatedResourceDir.map { it.file("META-INF/catalogs.properties") })
    property("version", project.version)
}

sourceSets {
    main {
        resources {
            srcDir {
                files(generatedResourceDir) {
                    builtBy(writeProps)
                }
            }
        }
    }
}

mavenPublishing {
    coordinates(project.group.toString(), project.name, project.version.toString())
    publishToMavenCentral(true)
    signAllPublications()

    pom {
        name.set(project.name)
        description.set(project.description)
        inceptionYear.set("2023")
        url.set("https://github.com/Lyxnx/android-gradle-catalogs")
        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("https://opensource.org/license/apache-2-0")
            }
        }
        developers {
            developer {
                id.set("Lyxnx")
                name.set("Lyxnx")
                url.set("https://github.com/Lyxnx")
            }
        }
        scm {
            url.set("https://github.com/Lyxnx/android-gradle-catalogs")
            connection.set("https://github.com/Lyxnx/android-gradle-catalogs.git")
            developerConnection.set("scm:git:ssh://git@github.com/Lyxnx/android-gradle-catalogs.git")
        }
    }
}
