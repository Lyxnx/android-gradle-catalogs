package io.github.lyxnx.gradle.resolver

import io.github.lyxnx.gradle.catalog.model.Library
import io.github.lyxnx.gradle.catalog.model.Plugin

data class DependencyResolverResult(
    val unresolved: DependencyCollection,
    val resolved: DependencyCollection,
    val excluded: DependencyCollection
) {
    data class DependencyCollection(
        val libraries: List<Library>,
        val plugins: List<Plugin>
    ) {
        fun isEmpty(): Boolean = libraries.isEmpty() && plugins.isEmpty()

        operator fun plus(other: DependencyCollection) = DependencyCollection(
            libraries = this.libraries + other.libraries,
            plugins = this.plugins + other.plugins
        )
    }
}
