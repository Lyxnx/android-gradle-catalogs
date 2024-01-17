package io.github.lyxnx.gradle.android.catalogs.compose

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.base.composeCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.AndroidCommonExtension
import io.github.lyxnx.gradle.android.catalogs.internal.android
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensureVersion
import io.github.lyxnx.gradle.android.catalogs.internal.findBooleanProperty
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

/**
 * Configures the basic Jetpack Compose compiler options
 *
 * Including:
 * - Adding the BOM and runtime dependency
 * - Enabling compose in the build features
 * - Setting the compiler extension version
 * - Configuring the compiler to generate reports and/or metrics
 *
 * This plugin should be used for a module that requires basic Compose functionality but does not need the UI side of Compose
 */
public class ComposeCompilerPlugin : CatalogsBasePlugin() {

    override fun Project.configureCatalogPlugin() {
        val catalog = composeCatalog

        dependencies {
            val bom = catalog.ensureLibrary("bom")
            implementation(platform(bom))

            implementation(catalog.ensureLibrary("runtime"))
        }

        android<AndroidCommonExtension> {
            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = catalog.ensureVersion("compiler").toString()
            }
        }

        val generateReports = shouldGenerateComposeReports()
        val generateMetrics = shouldGenerateComposeMetrics()

        if (generateReports || generateMetrics) {
            val reportsDir = getComposeReportsDir()

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    if (generateReports) {
                        freeCompilerArgs.addAll(
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${reportsDir.absolutePath}"
                        )
                    }
                    if (generateMetrics) {
                        freeCompilerArgs.addAll(
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${reportsDir.absolutePath}"
                        )
                    }
                }
            }
        }
    }
}

private const val DEFAULT_REPORT_DIR = "compose-compiler-reports"
private const val PROP_REPORTS_DIR = "catalogs.composeCompilerReportsDir"
private const val PROP_GENERATE_REPORTS = "catalogs.composeCompilerReports"
private const val PROP_GENERATE_METRICS = "catalogs.composeCompilerMetrics"

private fun Project.shouldGenerateComposeReports(): Boolean {
    return findBooleanProperty(PROP_GENERATE_REPORTS) == true
}

private fun Project.shouldGenerateComposeMetrics(): Boolean {
    return findBooleanProperty(PROP_GENERATE_METRICS) == true
}

private fun Project.getComposeReportsDir(): File {
    val prop = findProperty(PROP_REPORTS_DIR)?.toString()

    return if (prop.isNullOrBlank()) {
        File(layout.buildDirectory.get().asFile, DEFAULT_REPORT_DIR)
    } else {
        File(projectDir, prop)
    }
}
