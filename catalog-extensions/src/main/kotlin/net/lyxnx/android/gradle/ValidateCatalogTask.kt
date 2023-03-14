package net.lyxnx.android.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Usage
import org.gradle.api.internal.catalog.DefaultVersionCatalog
import org.gradle.api.internal.catalog.DependencyModel
import org.gradle.api.internal.catalog.PluginModel
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.named
import org.gradle.language.base.plugins.LifecycleBasePlugin

abstract class ValidateCatalogTask : DefaultTask() {

    @get:Input
    abstract val dependenciesModel: Property<DefaultVersionCatalog>

    init {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        description = "Validates version catalog"
    }

    @TaskAction
    fun validate() {
        val catalog = dependenciesModel.get()

        val configuration = createConfiguration()
        configuration.addLibraries(catalog)
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
        for (lib in catalog.libraries) {
            if (lib.version.toString().isEmpty()) {
                continue
            } else {
                project.dependencies.add(name, "${lib.group}:${lib.name}:${lib.version}")
            }
        }
    }

    private fun DefaultVersionCatalog.checkVersionsUsed() {
        val librariesVersions = libraries.map { it.versionRef }.toSet()
        val pluginsVersions = plugins.map { it.versionRef }.toSet()
        val unusedVersions = versionAliases - librariesVersions - pluginsVersions

        if (unusedVersions.isNotEmpty()) logger.warn("[WARNING] These versions are unused: $unusedVersions")
    }

    companion object {
        const val CONFIGURATION_NAME = "libraries"
    }
}

private val DefaultVersionCatalog.libraries: Sequence<DependencyModel>
    get() = libraryAliases.asSequence().map(::getDependencyData)

private val DefaultVersionCatalog.plugins: Sequence<PluginModel>
    get() = pluginAliases.asSequence().map(::getPlugin)