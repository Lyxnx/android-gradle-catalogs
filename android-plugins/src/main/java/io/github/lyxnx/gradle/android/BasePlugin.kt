package io.github.lyxnx.gradle.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import kotlin.reflect.KClass

abstract class BasePlugin<T : BaseExtension>(private val klass: KClass<T>, private val extensionName: String) :
    Plugin<Project> {

    protected lateinit var extension: T
        private set

    protected lateinit var commonExtension: CommonExtension<*, *, *, *, *>
        private set

    final override fun apply(target: Project) {
        commonExtension = target.ensureAndroidPlugins()
        extension = target.extensions.create(extensionName, klass.java, target)
        target.configure()
    }

    abstract fun Project.configure()

    protected fun Project.findCatalog(name: String): VersionCatalog {
        return extensions.getByType<VersionCatalogsExtension>().named(name)
    }
}

private fun Project.ensureAndroidPlugins(): CommonExtension<*, *, *, *, *> {
    val extension: CommonExtension<*, *, *, *, *>? =
        if (pluginManager.hasPlugin("com.android.application")) {
            extensions.getByType<ApplicationExtension>()
        } else if (pluginManager.hasPlugin("com.android.library")) {
            extensions.getByType<LibraryExtension>()
        } else {
            null
        }

    check(extension != null) {
        """
            The module ${this.name} must have the com.android.application or the com.android.library plugin applied.
            
            Apply the correct one for the module context and re-sync. 
        """.trimIndent()
    }

    return extension
}

interface BaseExtension
