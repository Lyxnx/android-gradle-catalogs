package io.github.lyxnx.gradle.android.catalogs.base

import org.gradle.api.Project
import org.gradle.kotlin.dsl.findPlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

public abstract class CatalogsBasePlugin : AbstractPlugin() {

    protected var isKmp: Boolean = false
        private set

    final override fun Project.configure() {
        val kotlinPlugin = plugins.findPlugin(KotlinMultiplatformPluginWrapper::class)
            ?: plugins.findPlugin(KotlinAndroidPluginWrapper::class)

        isKmp = kotlinPlugin is KotlinMultiplatformPluginWrapper

        configureCatalogPlugin()
    }

    protected abstract fun Project.configureCatalogPlugin()
}
