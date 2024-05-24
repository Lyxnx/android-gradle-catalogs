package io.github.lyxnx.gradle.catalogs

import org.gradle.api.InvalidUserDataException
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.gradle.api.initialization.resolve.MutableVersionCatalogContainer

internal fun MutableVersionCatalogContainer.createCatalog(
    name: String,
    catalog: String,
    block: VersionCatalogBuilder.() -> Unit
) = createIfNotExists(name) {
    from("io.github.lyxnx.gradle:versions-$catalog:${CatalogsPlugin.version}")
    block()
}

internal fun MutableVersionCatalogContainer.createIfNotExists(
    name: String,
    block: VersionCatalogBuilder.() -> Unit
): VersionCatalogBuilder = findByName(name) ?: create(name).apply(block)

internal fun RepositoryHandler.addOrIgnore(block: RepositoryHandler.() -> Unit) {
    try {
        block()
    } catch (_: InvalidUserDataException) {
    }
}
