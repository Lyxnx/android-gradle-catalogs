package io.github.lyxnx.gradle.android.hilt

import io.github.lyxnx.gradle.android.base.CatalogsBasePlugin
import io.github.lyxnx.gradle.android.base.ensureLibrary
import io.github.lyxnx.gradle.android.base.implementation
import io.github.lyxnx.gradle.android.base.kapt
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

public class AndroidHiltConfigPlugin : CatalogsBasePlugin() {

    override fun Project.configure() {
        ensurePlugin("dagger.hilt.android.plugin")
        ensurePlugin("org.jetbrains.kotlin.kapt")

        afterEvaluate {
            val catalog = findCatalog(baseExtension.commonCatalogName.get())

            dependencies {
                implementation(catalog.ensureLibrary("hilt"))
                @Suppress("DEPRECATION")
                kapt(catalog.ensureLibrary("hilt.compiler"))
            }
        }
    }
}
