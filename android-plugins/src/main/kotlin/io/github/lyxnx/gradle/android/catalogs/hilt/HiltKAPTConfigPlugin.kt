package io.github.lyxnx.gradle.android.catalogs.hilt

import io.github.lyxnx.gradle.android.catalogs.base.commonCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.KAPT_PLUGIN
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.kapt
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import javax.inject.Inject

public class HiltKAPTConfigPlugin @Inject constructor(pluginRegistry: PluginRegistry) :
    BaseHiltConfigPlugin(pluginRegistry) {

    override fun Project.configureHilt() {
        pluginRegistry.ensurePlugin("KAPT", KAPT_PLUGIN)
        apply(plugin = KAPT_PLUGIN)

        val catalog = commonCatalog

        dependencies {
            kapt(catalog.ensureLibrary("hilt.compiler"))
        }
    }

}