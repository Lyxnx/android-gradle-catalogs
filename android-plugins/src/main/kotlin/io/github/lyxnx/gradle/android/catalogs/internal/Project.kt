package io.github.lyxnx.gradle.android.catalogs.internal

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.kotlin.dsl.findByType
import org.gradle.plugin.use.PluginDependency

internal data class ResolvedCatalog(
    val name: String,
    val versions: Map<String, VersionConstraint>,
    val libraries: Map<String, MinimalExternalModuleDependency>,
    val plugins: Map<String, PluginDependency>,
)

internal fun Project.ensureCatalogLibrary(module: String): MinimalExternalModuleDependency {
    return findCatalogLibrary(module)
        ?: error("Could not find dependency '$module' in any catalog, make sure the containing catalog is registered")
}

internal fun Project.findCatalogLibrary(module: String): MinimalExternalModuleDependency? {
    val catalogs = resolveCatalogs()

    return catalogs.firstNotNullOfOrNull {
        it.libraries.values.firstOrNull { it.module.toString() == module }
    }
}

private fun Project.resolveCatalogs(): List<ResolvedCatalog> {
    val extension = extensions.findByType<VersionCatalogsExtension>() ?: return emptyList()

    val list = mutableListOf<ResolvedCatalog>()

    for (name in extension.catalogNames) {
        val catalog = extension.find(name).get()

        val versions = catalog.versionAliases.associateWith { catalog.findVersion(it).get() }
        val libraries = catalog.libraryAliases.associateWith { catalog.findLibrary(it).get().get() }
        val plugins = catalog.pluginAliases.associateWith { catalog.findPlugin(it).get().get() }

        list += ResolvedCatalog(name, versions, libraries, plugins)
    }

    return list
}

internal fun Project.findStringProperty(name: String): String? {
    return providers.gradleProperty(name).orNull
}

internal fun Project.findBooleanProperty(name: String): Boolean? {
    return findStringProperty(name)?.toBoolean()
}
