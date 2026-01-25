package io.github.lyxnx.gradle

import com.vanniktech.maven.publish.DeploymentValidation
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.catalog.DefaultVersionCatalog
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.catalog.VersionCatalogPlugin
import org.gradle.api.plugins.catalog.internal.CatalogExtensionInternal
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.language.base.plugins.LifecycleBasePlugin

class CatalogsPlugin : Plugin<Project> {

    companion object {
        const val PROP_GROUP = "catalogs.group"
        const val PROP_VERSION = "catalogs.version"
    }

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(BasePlugin::class)
            apply(VersionCatalogPlugin::class)
            apply(MavenPublishPlugin::class)
        }

        val extension = extensions.create(CatalogsExtension.NAME, CatalogsExtension::class)

        group = findStringProperty(PROP_GROUP) ?: error("$PROP_GROUP cannot be found. Make sure it is set")
        version =
            findStringProperty(PROP_VERSION) ?: error("$PROP_VERSION cannot be found. Make sure it is set")

        configureMavenPublish()

        afterEvaluate {
            configureCatalog()
//            configureValidateTask(extension.verificationExcludes.mapNotNull { it.normalizeAlias() })
        }

        tasks.withType(PublishToMavenRepository::class.java).configureEach {
            if (name.endsWith("ToMavenCentralRepository")) {
                dependsOn(tasks.getByName("${name.removeSuffix("ToMavenCentralRepository")}ToMavenLocal"))
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
        val extension = extensions.getByName<CatalogExtensionInternal>("catalog")

        extension.versionCatalog {
            from(files("libs.versions.toml"))
        }
    }

    private fun Project.configureValidateTask(excludes: List<String>) {
        val versionCatalog = extensions.getByName<CatalogExtensionInternal>("catalog").versionCatalog.get()
        val validateCatalog = tasks.register<ValidateCatalogTask>(ValidateCatalogTask.NAME) {
            dependenciesModel.set(versionCatalog)
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
