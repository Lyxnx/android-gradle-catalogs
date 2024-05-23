@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.gradle.catalogs

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.gradle.api.initialization.resolve.MutableVersionCatalogContainer
import java.util.Properties

internal const val NAME_SHARED = "shared"

/**
 * Plugin that only applies the shared catalog and can be used for non-android use cases
 */
class CatalogsPlugin : Plugin<Settings> {

    override fun apply(settings: Settings) {
        settings.gradle.settingsEvaluated {
            settings.dependencyResolutionManagement {
                repositories.addOrIgnore {
                    mavenCentral()
                }

                versionCatalogs {
                    shared()
                }
            }
        }
    }

    companion object {
        internal val version: String by lazy {
            val props = Properties()
            CatalogsPlugin::class.java.getResourceAsStream("/META-INF/catalogs.properties")?.use {
                props.load(it)
            }
            props["version"] as? String ?: throw GradleException("Could not get catalog settings version")
        }
    }
}

/**
 * Applies the shared catalog to the catalog collection with the given [name], which is `shared` by default, if it does not exist
 */
fun MutableVersionCatalogContainer.shared(
    name: String = NAME_SHARED,
    block: VersionCatalogBuilder.() -> Unit = {}
): VersionCatalogBuilder = createCatalog(name, "shared", block)
