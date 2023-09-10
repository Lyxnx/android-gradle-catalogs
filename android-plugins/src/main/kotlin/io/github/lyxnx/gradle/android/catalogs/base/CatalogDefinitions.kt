package io.github.lyxnx.gradle.android.catalogs.base

import io.github.lyxnx.gradle.android.catalogs.internal.findCatalog
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog

internal val Project.commonCatalog: VersionCatalog
    get() = findCatalog("catalogs.commonCatalogName", "common")

internal val Project.androidxCatalog: VersionCatalog
    get() = findCatalog("catalogs.androidxCatalogName", "androidx")

internal val Project.composeCatalog: VersionCatalog
    get() = findCatalog("catalogs.composeCatalogName", "compose")

internal val Project.firebaseCatalog: VersionCatalog
    get() = findCatalog("catalogs.firebaseCatalogName", "firebase")

private fun Project.findCatalog(property: String, default: String): VersionCatalog {
    return findCatalog(providers.gradleProperty(property).orNull ?: default)
}