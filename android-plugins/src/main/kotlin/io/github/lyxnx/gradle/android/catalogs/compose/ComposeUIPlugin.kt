package io.github.lyxnx.gradle.android.catalogs.compose

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.androidTestImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.debugImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.ensureCatalogLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures the Jetpack Compose for UI
 *
 * Performs all configuration from [ComposeCompilerPlugin] and adds the following UI related dependencies:
 *
 * - `ui`, `foundation`, `foundation-layout`, and `ui-tooling-preview` as `implementation` dependencies
 * - `ui-tooling` and `ui-test-manifest` as `debugImplementation` dependencies
 * - `ui-test-junit4` as `androidTestImplementation` dependency
 */
public class ComposeUIPlugin : CatalogsBasePlugin() {

    override fun Project.configureCatalogPlugin() {
        val config = plugins.apply(ComposeCompilerPlugin::class)

        dependencies {
            // Compiler plugin already adds the runtime library and BOM as regular implementation
            implementation(ensureCatalogLibrary("androidx.compose.ui:ui"))
            implementation(ensureCatalogLibrary("androidx.compose.foundation:foundation"))
            implementation(ensureCatalogLibrary("androidx.compose.foundation:foundation-layout"))

            implementation(ensureCatalogLibrary("androidx.compose.ui:ui-tooling-preview"))
            debugImplementation(ensureCatalogLibrary("androidx.compose.ui:ui-tooling"))

            androidTestImplementation(config.bom)
            androidTestImplementation(ensureCatalogLibrary("androidx.compose.ui:ui-test-junit4"))
            debugImplementation(ensureCatalogLibrary("androidx.compose.ui:ui-test-manifest"))
        }
    }
}
