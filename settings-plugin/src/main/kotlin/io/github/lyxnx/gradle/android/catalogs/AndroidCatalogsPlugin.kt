@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.gradle.android.catalogs

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.resolve.MutableVersionCatalogContainer
import java.util.Properties

class AndroidCatalogsPlugin : Plugin<Settings> {

    override fun apply(settings: Settings) {
        settings.gradle.settingsEvaluated {
            settings.dependencyResolutionManagement {
                if (repositories.isEmpty()) {
                    repositories.mavenCentral()
                    repositories.google()
                }

                // Add androidx and common version catalogs, those are required for every android project, compose is optional
                versionCatalogs {
                    create("shared") {
                        from("io.github.lyxnx.gradle:versions-shared:$version")
                    }
                    create("androidx") {
                        from("io.github.lyxnx.gradle:versions-androidx:$version")
                    }
                }
            }
        }
    }

    companion object {
        val version: String by lazy {
            val props = Properties()
            AndroidCatalogsPlugin::class.java.getResourceAsStream("/META-INF/android-catalogs.properties")?.use {
                props.load(it)
            }
            props["version"] as? String ?: throw GradleException("Could not get catalog settings version")

        }
    }
}

/**
 * Applies the compose catalog to the catalog collection with the given [name], which is `compose` by default
 */
fun MutableVersionCatalogContainer.composeCatalog(name: String = "compose") {
    create(name) {
        from("io.github.lyxnx.gradle:versions-compose:${AndroidCatalogsPlugin.version}")
    }
}
