package io.github.lyxnx.gradle.android.catalogs.compose

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.base.composeCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.androidTestImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.debugImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
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
        plugins.apply(ComposeCompilerPlugin::class)

        val catalog = composeCatalog

        dependencies {
            // Compiler plugin already adds the runtime library and BOM as regular implementation
            implementation(catalog.ensureLibrary("ui"))
            implementation(catalog.ensureLibrary("foundation"))
            implementation(catalog.ensureLibrary("foundation.layout"))

            implementation(catalog.ensureLibrary("ui.tooling.preview"))
            debugImplementation(catalog.ensureLibrary("ui.tooling"))

            val bom = catalog.ensureLibrary("bom")
            androidTestImplementation(platform(bom))
            androidTestImplementation(catalog.ensureLibrary("ui.test.junit4"))
            debugImplementation(catalog.ensureLibrary("ui.test.manifest"))
        }
    }
}
