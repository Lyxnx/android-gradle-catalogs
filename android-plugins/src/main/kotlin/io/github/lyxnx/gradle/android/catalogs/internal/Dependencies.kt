package io.github.lyxnx.gradle.android.catalogs.internal

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

internal fun DependencyHandlerScope.implementation(notation: Any) {
    add("implementation", notation)
}

internal fun DependencyHandlerScope.debugImplementation(notation: Any) {
    add("debugImplementation", notation)
}

internal fun DependencyHandlerScope.api(notation: Any) {
    add("api", notation)
}

internal fun DependencyHandlerScope.ksp(notation: Any) {
    add("ksp", notation)
}

@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("KSP should be used over KAPT where possible as KSP is up to 2x faster. Some libraries, however, have not yet converted to KSP yet so KAPT is unavoidable")
internal fun DependencyHandlerScope.kapt(notation: Any) {
    add("kapt", notation)
}

internal fun DependencyHandlerScope.testImplementation(notation: Any) {
    add("testImplementation", notation)
}

internal fun DependencyHandlerScope.androidTestImplementation(notation: Any) {
    add("androidTestImplementation", notation)
}

internal fun VersionCatalog.ensureLibrary(name: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(name).orElseThrow {
        IllegalStateException("Could not find library with name $name in catalog ${this.name}")
    }
}

internal fun VersionCatalog.ensureVersion(name: String): VersionConstraint {
    return findVersion(name).orElseThrow {
        IllegalStateException("Could not find version with name $name in catalog ${this.name}")
    }
}
