package io.github.lyxnx.gradle

import com.android.build.api.attributes.BuildTypeAttr
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedConfiguration
import org.gradle.api.artifacts.ResolvedDependency
import org.gradle.api.attributes.Category
import org.gradle.api.internal.catalog.DefaultVersionCatalog
import org.gradle.api.internal.catalog.DependencyModel
import org.gradle.api.internal.catalog.PluginModel
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
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

        val mapNames = { it: ResolvedDependency -> "- ${it.name}" }

        logger.lifecycle("Resolved libraries:")
        resolveLibraries(catalog).map(mapNames).forEach(logger::lifecycle)


        val plugins = resolvePlugins(catalog).map(mapNames)

        logger.lifecycle("")

        if (plugins.isNotEmpty()) {
            logger.lifecycle("Resolved plugins:")
            plugins.forEach(logger::lifecycle)
        }

        catalog.getUnusedVersions().let {
            if (it.isNotEmpty()) {
                logger.warn("")
                logger.warn("The following versions are unused:")
                it.forEach { logger.warn("- $it") }
            }
        }
    }

    private fun createConfiguration(name: String): Configuration {
        return project.configurations.create(name) {
            isTransitive = false
            isCanBeResolved = true

            attributes {
//                attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_API))
                attribute(BuildTypeAttr.ATTRIBUTE, project.objects.named(BuildTypeAttr::class.java, "release"))
                attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category::class.java, "library"))
            }

            resolutionStrategy.capabilitiesResolution.all {
                select(candidates.first())
            }
        }
    }

    private fun resolveConfiguration(name: String, block: Configuration.() -> Unit): ResolvedConfiguration =
        createConfiguration(name).apply(block).resolvedConfiguration

    private fun resolveLibraries(catalog: DefaultVersionCatalog): List<ResolvedDependency> =
        resolveConfiguration(LIBS_CONFIGURATION_NAME) { addLibraries(catalog) }.sortDependencies()

    private fun resolvePlugins(catalog: DefaultVersionCatalog): List<ResolvedDependency> =
        resolveConfiguration(PLUGINS_CONFIGURATION_NAME) { addPlugins(catalog) }.sortDependencies()

    private fun ResolvedConfiguration.sortDependencies(): List<ResolvedDependency> =
        firstLevelModuleDependencies.sortedBy { it.name }

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

    private fun DefaultVersionCatalog.getUnusedVersions(): List<String> {
        val librariesVersions = libraries.mapNotNull { it.library.versionRef }.toSet()
        val pluginsVersions = plugins.mapNotNull { it.plugin.versionRef }.toSet()

        return versionAliases - librariesVersions - pluginsVersions
    }

    companion object {
        const val LIBS_CONFIGURATION_NAME = "libraries"
        const val PLUGINS_CONFIGURATION_NAME = "plugins"

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
