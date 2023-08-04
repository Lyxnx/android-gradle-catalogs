package io.github.lyxnx.gradle.android

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import kotlin.jvm.optionals.getOrNull

internal fun DependencyHandlerScope.implementation(notation: Any) {
    add("implementation", notation)
}

internal fun DependencyHandlerScope.ksp(notation: Any) {
    add("ksp", notation)
}

internal fun DependencyHandlerScope.testImplementation(notation: Any) {
    add("testImplementation", notation)
}

internal fun DependencyHandlerScope.androidTestImplementation(notation: Any) {
    add("androidTestImplementation", notation)
}

internal fun VersionCatalog.ensureLibrary(name: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(name).getOrNull() ?: error("Could not find library with name $name in catalog ${this.name}")
}
