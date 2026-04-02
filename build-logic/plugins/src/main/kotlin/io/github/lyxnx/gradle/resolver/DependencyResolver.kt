package io.github.lyxnx.gradle.resolver

import io.github.lyxnx.gradle.catalog.COMMENT_TAG
import io.github.lyxnx.gradle.catalog.model.Comments
import io.github.lyxnx.gradle.catalog.model.Library
import io.github.lyxnx.gradle.catalog.model.Plugin
import io.github.lyxnx.gradle.catalog.model.VersionCatalog
import io.github.lyxnx.gradle.catalog.model.VersionDefinition
import io.github.lyxnx.gradle.catalog.model.resolveVersions
import io.github.lyxnx.gradle.normalizeAlias
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleIdentifier
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.attributes.Attribute
import org.gradle.api.logging.Logger
import java.util.concurrent.TimeUnit

private const val BOM_MARKER = "@bom"
private val BOM_REGEX = Regex("^${Regex.escape(COMMENT_TAG)} *${Regex.escape(BOM_MARKER)}.*")

class DependencyResolver(
    private val logger: Logger
) {

    fun resolve(
        project: Project,
        versionCatalog: VersionCatalog,
        excludedAliases: List<String>
    ): DependencyResolverResult {
        val resolvedCatalog = versionCatalog.resolveVersions()
        val dependencyHandler = project.dependencies

        val result = resolve(
            createConfiguration = { project.configurations.detachedConfiguration().configure() },
            dependencyHandler = dependencyHandler,
            resolvedCatalog = resolvedCatalog,
            excludedAliases = excludedAliases
        )

        if (result.unresolved.libraries.isNotEmpty()) {
            // Try a multiplatform config to see if that works
            logger.info("Attempting multiplatform config for ${result.unresolved.libraries.size} libraries")

            val kmpResult = resolve(
                createConfiguration = {
                    project.configurations.detachedConfiguration().configure().withMultiplatformAttributes()
                },
                dependencyHandler = dependencyHandler,
                resolvedCatalog = resolvedCatalog.copy(
                    versions = emptyMap(),
                    plugins = emptyMap(),
                    libraries = resolvedCatalog.libraries.filter { it.value in result.unresolved.libraries },
                    bundles = emptyMap(),
                    versionComments = Comments(),
                    libraryComments = Comments(),
                    bundleComments = Comments(),
                    pluginComments = Comments()
                ),
                excludedAliases = excludedAliases
            )

            return result.copy(
                resolved = result.resolved + kmpResult.resolved,
                unresolved = result.unresolved.copy(
                    libraries = result.unresolved.libraries - kmpResult.resolved.libraries
                )
            )
        }

        return result
    }

    private fun resolve(
        createConfiguration: () -> Configuration,
        dependencyHandler: DependencyHandler,
        resolvedCatalog: VersionCatalog,
        excludedAliases: List<String>
    ): DependencyResolverResult {
        val excludedLibs = mutableListOf<Library>()
        val excludedPlugins = mutableListOf<Plugin>()

        val libraryConfiguration = createConfiguration()
        val pluginConfiguration = createConfiguration()

        val resolvedLibraryData = resolve(
            configuration = libraryConfiguration,
            dependencies = resolvedCatalog.libraries.mapNotNull { (key, library) ->
                if (key.normalizeAlias() in excludedAliases) {
                    logger.info("Ignoring library key $key because it is excluded")
                    excludedLibs.add(library)
                    null
                } else {
                    when (val version = library.version) {
                        is VersionDefinition.Simple -> {
                            val comments = resolvedCatalog.libraryComments.commentsForKey(key)
                            val isBom = comments.any { it.matches(BOM_REGEX) }
                            if (isBom) {
                                logger.info("Found bom $key")
                                dependencyHandler.platform("${library.module}:${version.version}")
                            } else {
                                dependencyHandler.create("${library.module}:${version.version}")
                            }
                        }

                        is VersionDefinition.Unspecified -> {
                            dependencyHandler.create(library.module)
                        }

                        else -> null
                    }
                }
            }
        )

        val resolvedPluginData = resolve(
            configuration = pluginConfiguration,
            dependencies = resolvedCatalog.plugins.mapNotNull { (key, plugin) ->
                if (key.normalizeAlias() in excludedAliases) {
                    logger.info("Ignoring plugin key $key because it is excluded")
                    excludedPlugins.add(plugin)
                    null
                } else {
                    when (val version = plugin.version) {
                        is VersionDefinition.Simple -> {
                            dependencyHandler.create("${plugin.id}:${plugin.id}.gradle.plugin:${version.version}")
                        }

                        else -> null
                    }
                }
            }
        )

        return DependencyResolverResult(
            unresolved = DependencyResolverResult.DependencyCollection(
                libraries = resolvedCatalog.libraries.values.filter { lib ->
                    resolvedLibraryData.unresolved.any { it.module == lib.module }
                },
                plugins = resolvedCatalog.plugins.values.filter { pl ->
                    resolvedPluginData.unresolved.any { it.isPlugin && it.pluginId == pl.id }
                }
            ),
            resolved = DependencyResolverResult.DependencyCollection(
                libraries = resolvedCatalog.libraries.values.filter { lib ->
                    resolvedLibraryData.resolved.any { it.module == lib.module }
                },
                plugins = resolvedCatalog.plugins.values.filter { pl ->
                    resolvedPluginData.resolved.any { it.isPlugin && it.pluginId == pl.id }
                }
            ),
            excluded = DependencyResolverResult.DependencyCollection(
                libraries = excludedLibs,
                plugins = excludedPlugins,
            )
        )
    }

    private fun resolve(
        configuration: Configuration,
        dependencies: List<Dependency>
    ): ResolutionData {
        configuration.dependencies.addAll(dependencies)

        val resolvedConfig = configuration.resolvedConfiguration.lenientConfiguration

        val resolved = resolvedConfig.firstLevelModuleDependencies.map {
            DependencyData(
                group = it.module.id.group,
                name = it.module.id.name,
                resolvedVersion = it.module.id.version
            )
        }
        val unresolved = resolvedConfig.unresolvedModuleDependencies.map {
            DependencyData(it.selector.module)
        }

        return ResolutionData(
            resolved = resolved,
            unresolved = unresolved
        )
    }

    private fun Configuration.configure(): Configuration = apply {
        isTransitive = true
        isCanBeResolved = true
        isCanBeConsumed = false
        resolutionStrategy.cacheDynamicVersionsFor(60, TimeUnit.MINUTES)
        resolutionStrategy.capabilitiesResolution.all {
            select(candidates.first())
        }
    }

    private fun Configuration.withMultiplatformAttributes(): Configuration = attributes {
        // Specify the common type which all KMP libraries will have
        attribute(Attribute.of("org.jetbrains.kotlin.platform.type", String::class.java), "common")
    }
}

private data class ResolutionData(
    val resolved: List<DependencyData>,
    val unresolved: List<DependencyData>
)

private data class DependencyData(val group: String, val name: String, val resolvedVersion: String?) {
    constructor(identifier: ModuleIdentifier) : this(identifier.group, identifier.name, null)

    val module: String get() = "$group:$name"

    val isPlugin: Boolean get() = name.endsWith(".gradle.plugin")

    val pluginId: String get() = if (isPlugin) name.dropLast(".gradle.plugin".length) else error("Not a plugin: $module")
}
