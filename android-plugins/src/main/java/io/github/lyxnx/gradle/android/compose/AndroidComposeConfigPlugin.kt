package io.github.lyxnx.gradle.android.compose

import io.github.lyxnx.gradle.android.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.base.androidTestImplementation
import io.github.lyxnx.gradle.android.base.debugImplementation
import io.github.lyxnx.gradle.android.base.ensureLibrary
import io.github.lyxnx.gradle.android.base.ensureVersion
import io.github.lyxnx.gradle.android.base.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

public class AndroidComposeConfigPlugin : CatalogsBasePlugin() {

    override fun Project.configure() {
        val commonExtension = ensureAndroidExtension()
        val catalog = findCatalog(baseExtension.composeCatalogName.get())

        commonExtension.apply {
            buildFeatures {
                compose = true
            }

            packaging {
                resources.excludes.add("META-INF/*")
            }

            composeOptions {
                kotlinCompilerExtensionVersion = catalog.ensureVersion("compiler").toString()
            }

            dependencies {
                val bom = catalog.ensureLibrary("bom")
                implementation(platform(bom))

                implementation(catalog.ensureLibrary("runtime"))
                implementation(catalog.ensureLibrary("ui"))
                implementation(catalog.ensureLibrary("foundation"))
                implementation(catalog.ensureLibrary("foundation.layout"))

                implementation(catalog.ensureLibrary("ui.tooling.preview"))
                debugImplementation(catalog.ensureLibrary("ui.tooling.preview"))

                androidTestImplementation(platform(bom))
                androidTestImplementation(catalog.ensureLibrary("ui.test.junit4"))
                debugImplementation(catalog.ensureLibrary("ui.test.manifest"))
            }
        }
    }
}