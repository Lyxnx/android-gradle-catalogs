package io.github.lyxnx.gradle.android.catalogs.internal

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal fun Project.findCatalog(name: String): VersionCatalog {
    return extensions.getByType<VersionCatalogsExtension>().find(name).orElseThrow {
        throw IllegalStateException(
            """
                Could not find catalog with name $name. Make sure it is defined in the settings file like so:
                dependencyResolutionManagement {
                    versionCatalogs {
                        create("$name") {
                            from(<catalog location>)
                        }
                    }
                }
                
                If it does already exist, make sure the name in gradle.properties match that in the settings file (if changed from default)
                """.trimIndent()
        )
    }
}