package io.github.lyxnx.gradle.android

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

abstract class BasePlugin<T : BaseExtension> : Plugin<Project> {

    protected lateinit var extension: T
        private set

    protected lateinit var project: Project
        private set

    final override fun apply(target: Project) {
        project = target
        extension = createExtension()
        target.configure()
    }

    abstract fun createExtension(): T

    abstract fun Project.configure()

    protected inline fun <reified T : BaseExtension> createExtension(name: String): T {
        return project.extensions.create(name, T::class.java, project)
    }

    protected fun Project.findCatalog(name: String): VersionCatalog {
        return extensions.getByType<VersionCatalogsExtension>().named(name)
    }
}

interface BaseExtension
