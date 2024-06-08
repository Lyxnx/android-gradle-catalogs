package io.github.lyxnx.gradle.android.catalogs.compose

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.androidTestImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.debugImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.ensureCatalogLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import io.github.lyxnx.gradle.android.catalogs.internal.kotlinMulitplatform
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.compose.ExperimentalComposeLibrary

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

        if (config.isMultiplatform) {
            val composeDeps = composeDependencies

            kotlinMulitplatform {
                sourceSets.commonMain.dependencies {
                    // Compiler plugin already adds the runtime library
                    implementation(composeDeps.ui)
                    implementation(composeDeps.foundation)

                    // Not available on ios just yet
//                    implementation(composeDeps.uiTooling)
//                    implementation(composeDeps.preview)
                }

                @OptIn(ExperimentalComposeLibrary::class)
                sourceSets.commonTest.dependencies {
                    implementation(composeDeps.uiTest)
                }
            }
        } else {
            dependencies {
                // Compiler plugin already adds the runtime library and BOM as regular implementation
                implementation(ensureCatalogLibrary("androidx.compose.ui:ui"))
                implementation(ensureCatalogLibrary("androidx.compose.foundation:foundation"))
                implementation(ensureCatalogLibrary("androidx.compose.foundation:foundation-layout"))

                implementation(ensureCatalogLibrary("androidx.compose.ui:ui-tooling-preview"))
                debugImplementation(ensureCatalogLibrary("androidx.compose.ui:ui-tooling"))

                androidTestImplementation(platform(ensureCatalogLibrary("androidx.compose:compose-bom")))
                androidTestImplementation(ensureCatalogLibrary("androidx.compose.ui:ui-test-junit4"))
                debugImplementation(ensureCatalogLibrary("androidx.compose.ui:ui-test-manifest"))
            }
        }
    }
}
