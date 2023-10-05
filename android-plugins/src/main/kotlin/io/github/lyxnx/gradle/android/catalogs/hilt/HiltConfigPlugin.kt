package io.github.lyxnx.gradle.android.catalogs.hilt

import io.github.lyxnx.gradle.android.catalogs.internal.HILT_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.internal.KAPT_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.base.commonCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.kapt
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import javax.inject.Inject

public class HiltConfigPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry,
) : CatalogsBasePlugin() {

    override fun Project.configureCatalogPlugin() {
        pluginRegistry.ensurePlugin("Dagger Hilt", HILT_PLUGIN)
        pluginRegistry.ensurePlugin("KAPT", KAPT_PLUGIN)

        apply(plugin = HILT_PLUGIN)
        apply(plugin = KAPT_PLUGIN)

        val catalog = commonCatalog

        dependencies {
            implementation(catalog.ensureLibrary("hilt"))
            @Suppress("DEPRECATION")
            kapt(catalog.ensureLibrary("hilt.compiler"))
        }
    }
}
