@file:Suppress("UnstableApiUsage")

package io.github.lyxnx.gradle.catalogs.android

import io.github.lyxnx.gradle.catalogs.CatalogsPlugin
import io.github.lyxnx.gradle.catalogs.addOrIgnore
import io.github.lyxnx.gradle.catalogs.createCatalog
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.gradle.api.initialization.resolve.MutableVersionCatalogContainer
import org.gradle.kotlin.dsl.apply

internal const val NAME_ANDROIDX = "androidx"
internal const val NAME_COMPOSE = "compose"

class AndroidCatalogsPlugin : Plugin<Settings> {

    override fun apply(settings: Settings) = with(settings) {
        apply<CatalogsPlugin>()

        gradle.settingsEvaluated {
            settings.dependencyResolutionManagement {
                repositories.addOrIgnore {
                    google()
                    mavenCentral()
                }

                versionCatalogs {
                    androidx()
                }
            }
        }
    }
}

/**
 * Applies the androidx catalog to the catalog collection with the given [name], which is `androidx` by default, if it does not exist
 */
fun MutableVersionCatalogContainer.androidx(
    name: String = NAME_ANDROIDX,
    block: VersionCatalogBuilder.() -> Unit = {}
): VersionCatalogBuilder = createCatalog(name, "androidx", block)

/**
 * Applies the compose catalog to the catalog collection with the given [name], which is `compose` by default, if it does not exist
 */
fun MutableVersionCatalogContainer.compose(
    name: String = NAME_COMPOSE,
    block: VersionCatalogBuilder.() -> Unit = {}
): VersionCatalogBuilder = createCatalog(name, "compose", block)
