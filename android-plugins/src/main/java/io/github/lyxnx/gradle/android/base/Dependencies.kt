package io.github.lyxnx.gradle.android.base

import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * Adds [notation] to the dependencies as an 'implementation' dependency
 */
public fun DependencyHandlerScope.implementation(notation: Any) {
    add("implementation", notation)
}

public fun DependencyHandlerScope.debugImplementation(notation: Any) {
    add("debugImplementation", notation)
}

/**
 * Adds [notation] to the dependencies as an 'implementation' dependency
 */
public fun DependencyHandlerScope.api(notation: Any) {
    add("api", notation)
}

/**
 * Adds [notation] to the dependencies as an 'ksp' dependency
 */
public fun DependencyHandlerScope.ksp(notation: Any) {
    add("ksp", notation)
}

/**
 * Adds [notation] to the dependencies as an 'kapt' dependency
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("KSP should be used over KAPT where possible as KSP is up to 2x faster. Some libraries, however, have not yet converted to KSP yet so KAPT is unavoidable")
public fun DependencyHandlerScope.kapt(notation: Any) {
    add("kapt", notation)
}

/**
 * Adds [notation] to the dependencies as an 'testImplementation' dependency
 */
public fun DependencyHandlerScope.testImplementation(notation: Any) {
    add("testImplementation", notation)
}

/**
 * Adds [notation] to the dependencies as an 'androidTestImplementation' dependency
 */
public fun DependencyHandlerScope.androidTestImplementation(notation: Any) {
    add("androidTestImplementation", notation)
}

/**
 * Ensures the library named [name] exists within this catalog or errors if not found
 *
 * This can be used as a dependency notation:
 * ```
 * dependencies {
 *     implementation(catalog.ensureLibrary(name))
 * }
 * ```
 */
public fun VersionCatalog.ensureLibrary(name: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(name).orElseThrow {
        IllegalStateException("Could not find library with name $name in catalog ${this.name}")
    }
}

/**
 * Ensures a version named [name] exists within this catalog or errors if not found
 */
public fun VersionCatalog.ensureVersion(name: String): VersionConstraint {
    return findVersion(name).orElseThrow {
        IllegalStateException("Could not find version with name $name in catalog ${this.name}")
    }
}
