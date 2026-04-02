package io.github.lyxnx.gradle.catalog.model

data class VersionCatalog(
    val versions: Map<String, VersionDefinition>,
    val libraries: Map<String, Library>,
    val plugins: Map<String, Plugin>,
    val bundles: Map<String, List<String>>,
    val versionComments: Comments = Comments(),
    val libraryComments: Comments = Comments(),
    val bundleComments: Comments = Comments(),
    val pluginComments: Comments = Comments(),
) {
    internal val Versioned.resolvedVersion: VersionDefinition
        get() {
            if (versions.isEmpty()) {
                return version
            }
            val version = version
            return if (version is VersionDefinition.Reference) {
                versions[version.ref] ?: error("$this references undeclared version ${version.ref}")
            } else {
                version
            }
        }
}

data class Comments(
    val tableComments: List<String> = emptyList(),
    val entryComments: Map<String, List<String>> = emptyMap()
) {
    fun commentsForKey(key: String): List<String> = entryComments[key] ?: emptyList()
}

fun VersionCatalog.resolveVersions(): VersionCatalog {
    if (versions.isEmpty()) {
        return this
    }
    return copy(
        libraries = libraries.mapValues {
            it.value.copy(version = it.value.resolvedVersion)
        },
        plugins = plugins.mapValues {
            it.value.copy(version = it.value.resolvedVersion)
        }
    )
}
