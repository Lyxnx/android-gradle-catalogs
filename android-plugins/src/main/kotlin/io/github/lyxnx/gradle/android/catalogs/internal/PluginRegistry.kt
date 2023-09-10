package io.github.lyxnx.gradle.android.catalogs.internal

import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.plugin.use.internal.DefaultPluginId

internal fun PluginRegistry.hasPlugin(id: String): Boolean = lookup(DefaultPluginId.unvalidated(id)) != null

internal fun PluginRegistry.ensurePlugin(name: String, id: String) {
    check(hasPlugin(id)) {
        error(
            """
                $name plugin cannot be found. Make sure it is added as a build dependency to the project.
                E.g. Add using "apply false" to the root project:
                plugins {
                    id("$id") version "<version>" apply false
                }
                """.trimIndent()
        )
    }
}