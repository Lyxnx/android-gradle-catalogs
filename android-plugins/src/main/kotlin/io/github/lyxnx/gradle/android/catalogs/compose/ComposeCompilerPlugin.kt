package io.github.lyxnx.gradle.android.catalogs.compose

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.ensureCatalogLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensurePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.findBooleanProperty
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import io.github.lyxnx.gradle.android.catalogs.internal.kotlinMulitplatform
import org.gradle.api.Project
import org.gradle.api.internal.plugins.PluginRegistry
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.hasPlugin
import org.jetbrains.compose.ComposeBuildConfig
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import java.io.File
import javax.inject.Inject

private const val COMPOSE_DESKTOP_PLUGIN_ID = "org.jetbrains.compose"
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

    internal var isMultiplatform = false
        private set

    override fun Project.configureCatalogPlugin() {
        isMultiplatform = plugins.hasPlugin(KotlinMultiplatformPluginWrapper::class)

        // This one is needed for both
        pluginRegistry.ensurePlugin("Compose Compiler", COMPOSE_PLUGIN_ID)
        apply(plugin = COMPOSE_PLUGIN_ID)

        if (isMultiplatform) {
            pluginRegistry.ensurePlugin("Compose Desktop Compiler", COMPOSE_DESKTOP_PLUGIN_ID)
            apply(plugin = COMPOSE_DESKTOP_PLUGIN_ID)

            kotlinMulitplatform {
                sourceSets.commonMain.dependencies {
                    implementation("org.jetbrains.compose.runtime:runtime:${ComposeBuildConfig.composeVersion}")
                }
            }
        } else {
            dependencies {
                implementation(platform(ensureCatalogLibrary("androidx.compose:compose-bom")))

                implementation(ensureCatalogLibrary("androidx.compose.runtime:runtime"))
            }
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

internal val Project.composeDependencies get() = dependencies.extensions.getByType<ComposePlugin.Dependencies>()

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
