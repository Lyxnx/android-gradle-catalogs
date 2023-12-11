package io.github.lyxnx.gradle.android.catalogs.hilt

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.base.commonCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.HILT_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.internal.KSP_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import io.github.lyxnx.gradle.android.catalogs.internal.ksp
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import javax.inject.Inject

public class HiltConfigPlugin @Inject constructor(private val pluginRegistry: PluginRegistry) : CatalogsBasePlugin() {

    override fun Project.configureCatalogPlugin() {
        pluginRegistry.ensurePlugin("Dagger Hilt", HILT_PLUGIN)
        pluginRegistry.ensurePlugin("KSP", KSP_PLUGIN)

        apply(plugin = HILT_PLUGIN)
        apply(plugin = KSP_PLUGIN)

        val catalog = commonCatalog

        dependencies {
            implementation(catalog.ensureLibrary("hilt"))
            ksp(catalog.ensureLibrary("hilt.compiler"))
        }
    }

}