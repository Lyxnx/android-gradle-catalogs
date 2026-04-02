package io.github.lyxnx.gradle

import com.vanniktech.maven.publish.DeploymentValidation
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JvmEcosystemPlugin
import org.gradle.api.plugins.catalog.CatalogPluginExtension
import org.gradle.api.plugins.catalog.VersionCatalogPlugin
import org.gradle.api.provider.Provider
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.language.base.plugins.LifecycleBasePlugin

class CatalogsPlugin : Plugin<Project> {

    companion object {
        const val PROP_GROUP = "catalogs.group"
        const val PROP_VERSION = "catalogs.version"
    }

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            // Required for dependency resolution task as it doesn't resolve with just the normal version-catalog plugin
            // This is the lowest level plugin required and doesn't interfere with publication autodetection
            apply<JvmEcosystemPlugin>()
            apply<VersionCatalogPlugin>()
            apply<MavenPublishPlugin>()
        }

        val extension = extensions.create(CatalogsExtension.NAME, CatalogsExtension::class)

        group = findStringProperty(PROP_GROUP) ?: error("$PROP_GROUP cannot be found. Make sure it is set")
        version =
            findStringProperty(PROP_VERSION) ?: error("$PROP_VERSION cannot be found. Make sure it is set")

        configureMavenPublish()

        configureCatalog()

        configureValidateTask(extension.verificationExcludes.map { it.mapNotNull { it.normalizeAlias() } })

        tasks.withType<PublishToMavenRepository>().configureEach {
            val suffix = "ToMavenCentralRepository"
            if (name.endsWith(suffix)) {
                dependsOn(tasks.getByName("${name.removeSuffix(suffix)}ToMavenLocal"))
            }
        }
    }

    private fun Project.configureMavenPublish() {
        val extension = extensions.getByType<MavenPublishBaseExtension>()

        val projectGroup = this.group.toString()
        val projectName = this.name
        val projectVersion = this.version.toString()

        extension.apply {
            coordinates(projectGroup, projectName, projectVersion)
            publishToMavenCentral(automaticRelease = true, validateDeployment = DeploymentValidation.VALIDATED)
            signAllPublications()

            pom {
                name.set(projectName)
                description.set(this@configureMavenPublish.description)
                inceptionYear.set("2023")
                url.set("https://github.com/Lyxnx/android-gradle-catalogs")
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://opensource.org/license/apache-2-0")
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

    private fun Project.configureCatalog() {
        extensions.configure<CatalogPluginExtension> {
            versionCatalog {
                from(files("libs.versions.toml"))
            }
        }
    }

    private fun Project.configureValidateTask(excludes: Provider<List<String>>) {
        val validateCatalog = tasks.register<ValidateCatalogTask>(ValidateCatalogTask.NAME) {
            inputFile.set(project.layout.projectDirectory.file("libs.versions.toml"))
            this.excludes.set(excludes)
            dependsOn(tasks.named(VersionCatalogPlugin.GENERATE_CATALOG_FILE_TASKNAME))
        }
        tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME).configure { dependsOn(validateCatalog) }
        tasks.named(LifecycleBasePlugin.BUILD_TASK_NAME).configure { dependsOn(validateCatalog) }
    }
}

private fun Project.findStringProperty(name: String): String? {
    return providers.gradleProperty(name).orNull
}
