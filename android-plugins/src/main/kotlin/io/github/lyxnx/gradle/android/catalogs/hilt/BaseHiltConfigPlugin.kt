package io.github.lyxnx.gradle.android.catalogs.hilt

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.base.commonCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.HILT_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

public abstract class BaseHiltConfigPlugin internal constructor(
    protected val pluginRegistry: PluginRegistry
) : CatalogsBasePlugin() {

    final override fun Project.configureCatalogPlugin() {
        pluginRegistry.ensurePlugin("Dagger Hilt", HILT_PLUGIN)

        apply(plugin = HILT_PLUGIN)

        val catalog = commonCatalog

        dependencies {
            implementation(catalog.ensureLibrary("hilt"))
        }

        configureHilt()
    }

    protected abstract fun Project.configureHilt()
}