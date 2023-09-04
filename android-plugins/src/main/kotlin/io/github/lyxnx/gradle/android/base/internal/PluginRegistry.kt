package io.github.lyxnx.gradle.android.base.internal

import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.plugin.use.internal.DefaultPluginId

public fun PluginRegistry.hasPlugin(id: String): Boolean = lookup(DefaultPluginId.unvalidated(id)) != null