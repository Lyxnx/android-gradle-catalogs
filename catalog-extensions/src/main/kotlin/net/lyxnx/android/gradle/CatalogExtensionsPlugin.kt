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
            if (!plugins.hasPlugin("org.gradle.version-catalog")) {
                logger.warn(
                    """
                        Cannot apply plugin net.lyxnx.android.gradle.catalog-extensions:
                        Plugin org.gradle.version-catalog not found but is required
                    """.trimIndent()
                )
                return@with
            }

            apply<BasePlugin>()

            val extension = extensions.getByName<CatalogExtensionInternal>("catalog")
            val validateCatalog = tasks.register<ValidateCatalogTask>("validateCatalog") {
                dependenciesModel.set(extension.versionCatalog)
                dependsOn(tasks.named(GENERATE_CATALOG_FILE_TASKNAME))
            }
            tasks.named(CHECK_TASK_NAME).configure { dependsOn(validateCatalog) }
        }
    }
}