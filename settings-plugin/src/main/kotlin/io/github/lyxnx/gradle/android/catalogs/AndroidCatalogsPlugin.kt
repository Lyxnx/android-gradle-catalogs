@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.gradle.android.catalogs

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.gradle.api.initialization.resolve.MutableVersionCatalogContainer
import java.util.Properties

internal const val NAME_SHARED = "shared"
internal const val NAME_ANDROIDX = "androidx"
internal const val NAME_COMPOSE = "compose"

class AndroidCatalogsPlugin : Plugin<Settings> {

    override fun apply(settings: Settings) {
        settings.gradle.settingsEvaluated {
            settings.dependencyResolutionManagement {
                if (repositories.isEmpty()) {
                    repositories.mavenCentral()
                    repositories.google()
                }

                // Add default catalogs if they do not exist
                versionCatalogs {
                    if (!versionCatalogs.any { it.name == NAME_SHARED }) {
                        shared()
                    }

                    if (!versionCatalogs.any { it.name == NAME_ANDROIDX }) {
                        androidx()
                    }
                }
            }
        }
    }

    companion object {
        internal val version: String by lazy {
            val props = Properties()
            AndroidCatalogsPlugin::class.java.getResourceAsStream("/META-INF/android-catalogs.properties")?.use {
                props.load(it)
            }
            props["version"] as? String ?: throw GradleException("Could not get catalog settings version")

        }
    }
}

/**
 * Applies the shared catalog to the catalog collection with the given [name], which is `shared` by default
 */
fun MutableVersionCatalogContainer.shared(
    name: String = NAME_SHARED,
    block: VersionCatalogBuilder.() -> Unit = {}
) {
    createCatalog(name, "shared", block)
}

/**
 * Applies the androidx catalog to the catalog collection with the given [name], which is `androidx` by default
 */
fun MutableVersionCatalogContainer.androidx(
    name: String = NAME_ANDROIDX,
    block: VersionCatalogBuilder.() -> Unit = {}
) {
    createCatalog(name, "androidx", block)
}

/**
 * Applies the compose catalog to the catalog collection with the given [name], which is `compose` by default
 */
fun MutableVersionCatalogContainer.compose(
    name: String = NAME_COMPOSE,
    block: VersionCatalogBuilder.() -> Unit = {}
) {
    createCatalog(name, "compose", block)
}

private fun MutableVersionCatalogContainer.createCatalog(
    name: String,
    catalog: String,
    block: VersionCatalogBuilder.() -> Unit
) {
    create(name) {
        from("io.github.lyxnx.gradle:versions-$catalog:${AndroidCatalogsPlugin.version}")
        block()
    }
}
