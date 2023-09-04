package io.github.lyxnx.gradle.android.base

import com.android.build.api.variant.AndroidComponentsExtension
import io.github.lyxnx.gradle.android.base.internal.findByName
import io.github.lyxnx.gradle.android.base.internal.parents
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.getByType
import kotlin.reflect.KClass

/**
 * The base plugin class for all catalog plugins
 *
 * This makes it possible to strictly define extensions within the main [catalog extension][AndroidCatalogsExtension] namespace
 */
public abstract class CatalogsBasePlugin : Plugin<Project> {

    protected lateinit var baseExtension: AndroidCatalogsExtension
        private set

    protected lateinit var project: Project
        private set

    @PublishedApi
    internal val catalogExtensions: Sequence<AndroidCatalogsExtensionImpl>
        get() = project.parents.mapNotNull { it.extensions.androidGradleCatalogs }

    override fun apply(target: Project) {
        project = target
        project
        baseExtension = project.extensions.obtainCatalogsExtension()
        target.configure()
    }

    protected abstract fun Project.configure()

    protected inline fun <reified T : WithDefaults<T>> createExtension(
        name: String,
        publicType: KClass<in T>? = null,
        constructorArgs: Array<Any>? = null,
    ): T {
        val defaults = catalogExtensions
            .mapNotNull { it.extensions.findByName<T>(name) }
            .firstOrNull()
        return (baseExtension as ExtensionAware).extensions.createWithDefaults(
            name,
            defaults,
            publicType,
            constructorArgs
        )
    }

    private fun ExtensionContainer.obtainCatalogsExtension(): AndroidCatalogsExtension {
        return androidGradleCatalogs
            ?: createWithDefaults(AndroidCatalogsExtension.NAME, catalogExtensions.firstOrNull())
    }

    /**
     * Finds a catalog with [name]
     */
    protected fun Project.findCatalog(name: String): VersionCatalog {
        return extensions.getByType<VersionCatalogsExtension>().find(name).orElseThrow {
            throw IllegalStateException(
                """
                Could not find catalog with name $name. Make sure it is defined in the settings file like so:
                dependencyResolutionManagement {
                    versionCatalogs {
                        create($name) {
                            from(<catalog location>)
                        }
                    }
                }
                """.trimIndent()
            )
        }
    }

    protected val Project.androidComponents: AndroidComponentsExtension<*, *, *>
        get() = try {
            extensions.getByType(AndroidComponentsExtension::class.java)
        } catch (e: UnknownDomainObjectException) {
            error(
                """
                    Android Gradle Plugin not found. Make sure it is added as a build dependency to the project:
                    Add AGP with "apply false" to the root project:
                        plugins {
                            id("com.android.application") version <version> apply false
                        }
                """.trimIndent()
            )
        }

    protected fun Project.ensurePlugin(id: String) {
        check(pluginManager.hasPlugin(id)) {
            """
            $id plugin not found. Make sure it is added as a build dependency to the project
            Solution:
            Add it with "apply false" to the root project:
                plugins {
                    id("$id") version <version> apply false
                }
            """.trimIndent()
        }
    }
}