package io.github.lyxnx.gradle.android.catalogs.compose

import io.github.lyxnx.gradle.android.catalogs.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.catalogs.internal.androidTestImplementation
import io.github.lyxnx.gradle.android.catalogs.base.composeCatalog
import io.github.lyxnx.gradle.android.catalogs.internal.debugImplementation
import io.github.lyxnx.gradle.android.catalogs.internal.ensureLibrary
import io.github.lyxnx.gradle.android.catalogs.internal.ensureVersion
import io.github.lyxnx.gradle.android.catalogs.internal.implementation
import io.github.lyxnx.gradle.android.catalogs.internal.AndroidCommonExtension
import io.github.lyxnx.gradle.android.catalogs.internal.android
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

public class AndroidComposeConfigPlugin : CatalogsBasePlugin() {

    override fun Project.configureCatalogPlugin() {
        val catalog = composeCatalog

        dependencies {
            val bom = catalog.ensureLibrary("bom")
            implementation(platform(bom))

            implementation(catalog.ensureLibrary("runtime"))
            implementation(catalog.ensureLibrary("ui"))
            implementation(catalog.ensureLibrary("foundation"))
            implementation(catalog.ensureLibrary("foundation.layout"))

            implementation(catalog.ensureLibrary("ui.tooling.preview"))
            debugImplementation(catalog.ensureLibrary("ui.tooling"))

            androidTestImplementation(platform(bom))
            androidTestImplementation(catalog.ensureLibrary("ui.test.junit4"))
            debugImplementation(catalog.ensureLibrary("ui.test.manifest"))
        }

        android<AndroidCommonExtension> {
            buildFeatures {
                compose = true
            }

            packaging {
                resources.excludes.add("META-INF/*")
            }

            composeOptions {
                kotlinCompilerExtensionVersion = catalog.ensureVersion("compiler").toString()
            }
        }
    }
}