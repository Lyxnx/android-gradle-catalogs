package io.github.lyxnx.gradle.catalog

import io.github.lyxnx.gradle.catalog.model.Comments
import io.github.lyxnx.gradle.catalog.model.Library
import io.github.lyxnx.gradle.catalog.model.Plugin
import io.github.lyxnx.gradle.catalog.model.VersionCatalog
import io.github.lyxnx.gradle.catalog.model.VersionDefinition
import org.gradle.api.GradleException
import org.tomlj.Toml
import org.tomlj.TomlArray
import org.tomlj.TomlTable
import java.io.BufferedReader
import java.io.InputStream
import java.io.StringReader

const val COMMENT_TAG = "#"

private val TABLE_REGEX = Regex("\\[\\s?(versions|libraries|bundles|plugins)\\s?].*")
private val KEY_REGEX = Regex("^(.*?)=.*")

private const val TABLE_VERSIONS = "versions"
private const val TABLE_LIBRARIES = "libraries"
private const val TABLE_PLUGINS = "plugins"
private const val TABLE_BUNDLES = "bundles"

class VersionCatalogParser {

    fun parse(input: InputStream): VersionCatalog {
        val content = input.bufferedReader().use { it.readText() }
        val result = Toml.parse(content)
        if (result.hasErrors()) {
            throw GradleException("Cannot parse version catalog: ${result.errors()}")
        }

        val versionMap = result.getTable(TABLE_VERSIONS)?.toVersionDefinitionMap() ?: emptyMap()
        val dependencyMap = result.getTable(TABLE_LIBRARIES)?.toDependencyMap() ?: emptyMap()
        val pluginMap = result.getTable(TABLE_PLUGINS)?.toPluginMap() ?: emptyMap()
        val bundles = result.getTable(TABLE_BUNDLES)?.entrySet()?.associate { (key, value) ->
            key to (value as TomlArray).toList().map { it as String }
        } ?: emptyMap()

        return processComments(
            content,
            VersionCatalog(
                versions = versionMap,
                libraries = dependencyMap,
                plugins = pluginMap,
                bundles = bundles
            )
        )
    }

    private fun processComments(
        content: String,
        catalog: VersionCatalog
    ): VersionCatalog {
        val comments = mutableMapOf<String, Comments>()
        val currentComment = mutableListOf<String>()
        var currentTable: String? = null
        val reader = BufferedReader(StringReader(content))
        do {
            val line = reader.readLine() ?: break
            when {
                line.startsWith(COMMENT_TAG) -> currentComment.add(line)
                line.trim().matches(TABLE_REGEX) -> {
                    val table = TABLE_REGEX.find(line)!!.groupValues[1]
                    currentTable = table
                    comments[table] = Comments(currentComment.toList(), emptyMap())
                    currentComment.clear()
                }

                line.matches(KEY_REGEX) && currentTable != null && currentComment.isNotEmpty() -> {
                    val key = KEY_REGEX.find(line)!!.groupValues[1].trim()
                    val currentComments = comments[currentTable] ?: error("Should have an entry")
                    comments[currentTable] = currentComments.copy(
                        entryComments = currentComments.entryComments + mapOf(key to currentComment.toList())
                    )
                    currentComment.clear()
                }
            }
        } while (true)

        return catalog.copy(
            versionComments = comments[TABLE_VERSIONS] ?: Comments(),
            libraryComments = comments[TABLE_LIBRARIES] ?: Comments(),
            pluginComments = comments[TABLE_PLUGINS] ?: Comments(),
            bundleComments = comments[TABLE_BUNDLES] ?: Comments()
        )
    }

}

private fun TomlTable.toVersionDefinitionMap(): Map<String, VersionDefinition> {
    return this.entrySet().associate { (key, value) ->
        key to when (val version = value.toVersion()) {
            null -> throw IllegalStateException("Unable to parse version $value")
            is VersionDefinition.Reference -> throw IllegalStateException("Version $key cannot be a reference")
            else -> version
        }
    }
}

private fun TomlTable.toDependencyMap(): Map<String, Library> {
    return this.entrySet().associate { (key, value) ->
        key to when (value) {
            is String -> {
                val components = value.split(":")
                if (components.size != 3) {
                    throw IllegalStateException("Invalid dependency definition for $key: $value")
                }
                val (group, module, version) = components
                Library(
                    group = group,
                    name = module,
                    version = VersionDefinition.Simple(version)
                )
            }

            is TomlTable -> {
                val group = value.getString("group")
                val module = value.getString("module")
                val name = value.getString("name")
                @Suppress("MISSING_DEPENDENCY_IN_INFERRED_TYPE_ANNOTATION_WARNING")
                val version = value.get("version")
                    ?.let { it.toVersion() ?: throw IllegalStateException("Unrecognised version for $key") }
                    ?: VersionDefinition.Unspecified
                if (module == null && (group == null || name == null)) {
                    throw IllegalStateException("$key should define module or group/name")
                }
                if (module != null && (group != null || name != null)) {
                    throw IllegalStateException("$key should only define module or group/name combination")
                }
                if (module != null) {
                    Library(
                        module = module,
                        version = version
                    )
                } else {
                    Library(
                        group = requireNotNull(group),
                        name = requireNotNull(name),
                        version = version
                    )
                }
            }

            else -> throw IllegalStateException("Unsupported type ${value::class.java}")
        }
    }
}

private fun TomlTable.toPluginMap(): Map<String, Plugin> {
    return this.entrySet().associate { (key, value) ->
        key to when (value) {
            is String -> {
                val components = value.split(":")
                if (components.size != 2) {
                    throw IllegalStateException("Invalid plugin definition for $key: $value")
                }
                val (id, version) = components
                Plugin(
                    id = id,
                    version = VersionDefinition.Simple(version)
                )
            }

            is TomlTable -> {
                val id = value.getString("id")
                val version = value.get("version")?.toVersion() ?: VersionDefinition.Unspecified

                if (id == null) {
                    throw IllegalStateException("No plugin ID defined for $key")
                }

                Plugin(
                    id = id,
                    version = version
                )
            }

            else -> throw IllegalStateException("Unsupported type ${value::class.java}")
        }
    }
}

private fun Any.toVersion(): VersionDefinition? = when (this) {
    is String -> VersionDefinition.Simple(this)
    is TomlTable -> {
        if (size() == 1 && contains("ref")) {
            VersionDefinition.Reference(getString("ref")!!)
        } else {
            null
        }
    }

    else -> null
}
