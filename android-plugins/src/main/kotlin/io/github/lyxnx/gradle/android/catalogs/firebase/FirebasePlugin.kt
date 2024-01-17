package io.github.lyxnx.gradle.android.catalogs.firebase

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.base.firebaseCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.GMS_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import javax.inject.Inject

/**
 * Configures Firebase for a module
 *
 * This will apply the `com.google.gms.google-services` plugin and add the Firebase BOM as a dependency, so the only
 * thing left to do is add the needed Firebase libraries
 */
public class FirebasePlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : CatalogsBasePlugin() {

    override fun Project.configureCatalogPlugin() {
        pluginRegistry.ensurePlugin("Google GMS", GMS_PLUGIN)
        apply(plugin = GMS_PLUGIN)

        val catalog = firebaseCatalog

        dependencies {
            implementation(platform(catalog.ensureLibrary("bom")))
        }
    }
}
