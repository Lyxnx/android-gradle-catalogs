package io.github.lyxnx.gradle.android.catalogs.compose

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.ensureCatalogLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.findBooleanProperty
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import java.io.File
import javax.inject.Inject

private const val COMPOSE_PLUGIN_ID = "org.jetbrains.kotlin.plugin.compose"

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
public class ComposeCompilerPlugin @Inject constructor(
    private val pluginRegistry: PluginRegistry
) : CatalogsBasePlugin() {

    internal lateinit var bom: Dependency
        private set

    override fun Project.configureCatalogPlugin() {
        pluginRegistry.ensurePlugin("Compose Compiler", COMPOSE_PLUGIN_ID)
        apply(plugin = COMPOSE_PLUGIN_ID)

        dependencies {
            bom = platform(ensureCatalogLibrary("androidx.compose:compose-bom"))
            implementation(bom)

            implementation(ensureCatalogLibrary("androidx.compose.runtime:runtime"))
        }

        val generateReports = shouldGenerateComposeReports()
        val generateMetrics = shouldGenerateComposeMetrics()

        if (generateReports || generateMetrics) {
            val reportsDir = getComposeReportsDir()

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                if (generateReports) {
                    reportsDestination = reportsDir
                }
                if (generateMetrics) {
                    metricsDestination = reportsDir
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
