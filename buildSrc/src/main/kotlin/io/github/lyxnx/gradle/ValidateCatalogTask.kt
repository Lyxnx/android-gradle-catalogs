package io.github.lyxnx.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Usage
import org.gradle.api.internal.catalog.DefaultVersionCatalog
import org.gradle.api.internal.catalog.DependencyModel
import org.gradle.api.internal.catalog.PluginModel
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.named
import org.gradle.language.base.plugins.LifecycleBasePlugin

abstract class ValidateCatalogTask : DefaultTask() {

    @get:Input
    abstract val dependenciesModel: Property<DefaultVersionCatalog>

    @get:Input
    abstract val excludes: ListProperty<String>

    init {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        description = "Validates version catalog"
    }

    @TaskAction
    fun validate() {
        val catalog = dependenciesModel.get()

        val configuration = createConfiguration()
        configuration.addLibraries(catalog)
        configuration.addPlugins(catalog)

        logger.info("Resolved dependencies:")
        configuration.resolvedConfiguration
            .firstLevelModuleDependencies
            .forEach { logger.info("- ${it.name}") }

        catalog.checkVersionsUsed()
    }

    private fun createConfiguration(): Configuration {
        return project.configurations.create(CONFIGURATION_NAME) {
            isTransitive = false
            isCanBeResolved = true

            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_API))
            }

            resolutionStrategy.capabilitiesResolution.all {
                select(candidates.first())
            }
        }
    }

    private fun Configuration.addLibraries(catalog: DefaultVersionCatalog) {
        for ((alias, lib) in catalog.libraries) {
            if ((lib.versionRef != null && excludes.get().contains(alias)) || lib.version.toString().isEmpty())
                continue

            project.dependencies.add(name, "${lib.group}:${lib.name}:${lib.version}")
        }
    }

    private fun Configuration.addPlugins(catalog: DefaultVersionCatalog) {
        /*
        We don't have the plugins registered in a root buildscript like you normally would with "apply false", nor is
        there a way to add a plugin with and ID and version using pluginManager.apply

        Since there's no easy way to resolve the dependency from a plugin ID, we can try adding it as a regular dependency
        to make sure it resolves

        From the gradle docs:

        Gradle will look for a Plugin Marker Artifact with the coordinates plugin.id:plugin.id.gradle.plugin:plugin.version
         */
        for ((alias, plugin) in catalog.plugins) {
            if ((plugin.versionRef != null && excludes.get().contains(alias)) || plugin.version.toString().isEmpty()) {
                continue
            }

            project.dependencies.add(name, "${plugin.id}:${plugin.id}.gradle.plugin:${plugin.version.toString()}")
        }
    }

    private fun DefaultVersionCatalog.checkVersionsUsed() {
        val librariesVersions = libraries.map { it.library.versionRef }.toSet()
        val pluginsVersions = plugins.map { it.plugin.versionRef }.toSet()
        val unusedVersions = versionAliases - librariesVersions - pluginsVersions

        if (unusedVersions.isNotEmpty()) logger.warn("[WARNING] These versions are unused: $unusedVersions")
    }

    companion object {
        const val CONFIGURATION_NAME = "libraries"
        const val NAME = "validateCatalog"
    }
}

private val DefaultVersionCatalog.libraries: Sequence<DependencyInfo>
    get() = libraryAliases.asSequence().map { DependencyInfo(it, getDependencyData(it)) }

private val DefaultVersionCatalog.plugins: Sequence<PluginInfo>
    get() = pluginAliases.asSequence().map { PluginInfo(it, getPlugin(it)) }

/**
 * Represents a dependency
 *
 * @property alias the alias used within the catalog
 * @property library the resolved dependency
 */
data class DependencyInfo(val alias: String, val library: DependencyModel)

/**
 * Represents a plugin
 *
 * @property alias the alias used within the catalog
 * @property plugin the resolved plugin
 */
data class PluginInfo(val alias: String, val plugin: PluginModel)
