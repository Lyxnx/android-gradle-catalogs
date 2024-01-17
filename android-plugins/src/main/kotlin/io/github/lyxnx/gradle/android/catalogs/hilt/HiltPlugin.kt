package io.github.lyxnx.gradle.android.catalogs.hilt

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.base.commonCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.HILT_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.internal.KSP_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.internal.androidTestImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import io.github.lyxnx.gradle.android.catalogs.internal.ksp
import io.github.lyxnx.gradle.android.catalogs.internal.testImplementation
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import javax.inject.Inject

/**
 * Configures Dagger Hilt for a module
 *
 * This will apply the `com.google.devtools.ksp` and `dagger.hilt.android.plugin` plugins and add the hilt library as a
 * dependency
 *
 * This will also apply the hilt testing dependencies to both the `testImplementation` and `androidTestImplementation`
 * configurations
 */
public class HiltPlugin @Inject constructor(private val pluginRegistry: PluginRegistry) : CatalogsBasePlugin() {

    override fun Project.configureCatalogPlugin() {
        pluginRegistry.ensurePlugin("Dagger Hilt", HILT_PLUGIN)
        pluginRegistry.ensurePlugin("KSP", KSP_PLUGIN)

        apply(plugin = HILT_PLUGIN)
        apply(plugin = KSP_PLUGIN)

        val catalog = commonCatalog

        dependencies {
            implementation(catalog.ensureLibrary("hilt"))
            ksp(catalog.ensureLibrary("hilt.compiler"))

            testImplementation(catalog.ensureLibrary("hilt.testing"))
            androidTestImplementation(catalog.ensureLibrary("hilt.testing"))
        }
    }

}
