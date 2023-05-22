package net.lyxnx.android.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.catalog.VersionCatalogPlugin.GENERATE_CATALOG_FILE_TASKNAME
import org.gradle.api.plugins.catalog.internal.CatalogExtensionInternal
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.register
import org.gradle.language.base.plugins.LifecycleBasePlugin.CHECK_TASK_NAME

class CatalogExtensionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.gradle.version-catalog")

            group = "net.lyxnx.android"
            version = "2023.05.22"

            apply<BasePlugin>()

            val extension = extensions.getByName<CatalogExtensionInternal>("catalog")

            extension.versionCatalog {
                from(files("libs.versions.toml"))
            }

            val validateCatalog = tasks.register<ValidateCatalogTask>("validateCatalog") {
                dependenciesModel.set(extension.versionCatalog)
                dependsOn(tasks.named(GENERATE_CATALOG_FILE_TASKNAME))
            }
            tasks.named(CHECK_TASK_NAME).configure { dependsOn(validateCatalog) }
        }
    }
}
