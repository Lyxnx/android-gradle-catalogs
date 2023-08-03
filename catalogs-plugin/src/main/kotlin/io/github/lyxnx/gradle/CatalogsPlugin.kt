package io.github.lyxnx.gradle

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.catalog.VersionCatalogPlugin
import org.gradle.api.plugins.catalog.internal.CatalogExtensionInternal
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.language.base.plugins.LifecycleBasePlugin

@Suppress("UnstableApiUsage")
class CatalogsPlugin : Plugin<Project> {

    companion object {
        const val GROUP_PROPERTY = "CATALOGS_GROUP"
        const val VERSION_PROPERTY = "CATALOGS_VERSION"
    }

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(BasePlugin::class)
                apply(VersionCatalogPlugin::class)
                apply(MavenPublishPlugin::class)
            }

            group = findStringProperty(GROUP_PROPERTY) ?: error("$GROUP_PROPERTY can not be found. Make sure it is set")
            version =
                findStringProperty(VERSION_PROPERTY) ?: error("$VERSION_PROPERTY can not be found. Make sure it is set")

            configureMavenPublish()

            configureValidateTask()
        }
    }

    private fun Project.configureMavenPublish() {
        val extension = extensions.getByType<MavenPublishBaseExtension>()

        val projectGroup = this.group.toString()
        val projectName = this.name
        val projectVersion = this.version.toString()

        extension.apply {
            coordinates(projectGroup, projectName, projectVersion)

            publishToMavenCentral(SonatypeHost.Companion.S01, true)
            signAllPublications()

            pom {
                name.set(projectName)
                description.set(this@configureMavenPublish.description)
                inceptionYear.set("2023")
                url.set("https://github.com/Lyxnx/android-gradle-catalogs")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }
                developers {
                    developer {
                        id.set("Lyxnx")
                        name.set("Lyxnx")
                        url.set("https://github.com/Lyxnx")
                    }
                }
                scm {
                    url.set("https://github.com/Lyxnx/android-gradle-catalogs")
                    connection.set("https://github.com/Lyxnx/android-gradle-catalogs.git")
                    developerConnection.set("scm:git:ssh://git@github.com/Lyxnx/android-gradle-catalogs.git")
                }
            }
        }
    }

    private fun Project.configureValidateTask() {
        val extension = extensions.getByName<CatalogExtensionInternal>("catalog")

        extension.versionCatalog {
            from(files("libs.versions.toml"))
        }

        val validateCatalog = tasks.register<ValidateCatalogTask>(ValidateCatalogTask.NAME) {
            dependenciesModel.set(extension.versionCatalog)
            dependsOn(tasks.named(VersionCatalogPlugin.GENERATE_CATALOG_FILE_TASKNAME))
        }
        tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME).configure { dependsOn(validateCatalog) }
    }
}

private fun Project.findStringProperty(name: String): String? {
    return providers.gradleProperty(name).orNull
}
